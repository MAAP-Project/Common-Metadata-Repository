(ns cmr.indexer.data.concepts.software
  "Contains functions to parse and convert software concept"
  (:require
    [cheshire.core :as json]
    [clj-time.core :as t]
    [clojure.set :as set]
    [clojure.string :as str]
    [cmr.acl.acl-fetcher :as acl-fetcher]
    [cmr.common-app.config :as common-config]
    [cmr.common-app.services.kms-fetcher :as kf]
    [cmr.common.concepts :as concepts]
    [cmr.common.log :refer (debug info warn error)]
    [cmr.common.mime-types :as mt]
    [cmr.common.services.errors :as errors]
    [cmr.common.time-keeper :as tk]
    [cmr.common.util :as util]
    [cmr.elastic-utils.index-util :as index-util]
    [cmr.indexer.data.collection-granule-aggregation-cache :as cgac]
    [cmr.indexer.data.concepts.attribute :as attrib]
    [cmr.indexer.data.concepts.collection.collection-util :as collection-util]
    [cmr.indexer.data.concepts.collection.community-usage-metrics :as metrics]
    [cmr.indexer.data.concepts.collection.data-center :as data-center]
    [cmr.indexer.data.concepts.collection.humanizer :as humanizer]
    [cmr.indexer.data.concepts.collection.instrument :as instrument]
    [cmr.indexer.data.concepts.collection.keyword :as k]
    [cmr.indexer.data.concepts.collection.location-keyword :as clk]
    [cmr.indexer.data.concepts.collection.opendata :as opendata]
    [cmr.indexer.data.concepts.collection.platform :as platform]
    [cmr.indexer.data.concepts.software.science-keyword :as sk]
    [cmr.indexer.data.concepts.service :as service]
    [cmr.indexer.data.concepts.spatial :as spatial]
    [cmr.indexer.data.concepts.tag :as tag]
    [cmr.indexer.data.concepts.variable :as variable]
    [cmr.indexer.data.elasticsearch :as es]
    [cmr.umm-spec.acl-matchers :as umm-matchers]
    [cmr.umm-spec.date-util :as date-util]
    [cmr.umm-spec.location-keywords :as lk]
    [cmr.umm-spec.models.umm-collection-models :as umm-collection]
    [cmr.umm-spec.related-url :as ru]
    [cmr.umm-spec.time :as spec-time]
    [cmr.umm-spec.umm-spec-core :as umm-spec]
    [cmr.umm-spec.util :as su]
    [cmr.umm.collection.entry-id :as eid]
    [cmr.umm.umm-collection :as umm-c]
    [cmr.umm-spec.util :as spec-util]))

(defn safe-trim
  "Safely cut the space at the beginning and the end of string"
  [v]
  (try
  (when (some? v) (str/trim v))
  (catch Throwable e
      (error e "Error in Safe-trim: " v))
   )
)

(defn- assoc-nil-if
  "Set value to nil if the predicate is true
  Uses assoc."
  [software key predicate]
  (if predicate
    (assoc software key nil)
    software))

(defn- assoc-in-nil-if
  "Set value to nil if the predicate is true.
  Uses assoc-in."
  [software keys predicate]
  (if predicate
    (assoc-in software keys nil)
    software))

(defn- sanitize-processing-level-ids
  "Sanitize Processing Level Ids if and only if the values are default"
  [software]
  (assoc-in-nil-if software
                   [:ProcessingLevel :Id]
                   (= (get-in software [:ProcessingLevel :Id]) su/not-provided)))

(defn- remove-index-irrelevant-defaults
  "Remove default values irrelevant to indexing to avoid them being indexed"
  [software]
  (-> software
      sanitize-processing-level-ids
      (assoc-nil-if :Platforms (= (:Platforms software) su/not-provided-platforms))
      (assoc-nil-if :RelatedUrls (= (:RelatedUrls software) [su/not-provided-related-url]))
      (assoc-nil-if :ScienceKeywords (= (:ScienceKeywords software) su/not-provided-science-keywords))
      (assoc-nil-if :DataCenters (= (:DataCenters software) [su/not-provided-data-center]))))

(defn- get-coll-permitted-group-ids
  "Returns the groups ids (group guids, 'guest', 'registered') that have permission to read
  this software"
  [context provider-id soft]
  (->> (acl-fetcher/get-acls context [:catalog-item])
       ;; Find only acls that are applicable to this software
       (filter (partial umm-matchers/coll-applicable-acl? provider-id soft))
       ;; Get the permissions they grant
       (mapcat :group-permissions)
       ;; Find permissions that grant read
       (filter #(some (partial = "read") (:permissions %)))
       ;; Get the group guids or user type of those permissions
       (map #(or (:group-id %) (some-> % :user-type name)))
       distinct))

(defn- get-elastic-doc-for-full-software
  "Get all the fields for a normal software index operation."
  [context concept software ]
  (let [{:keys [concept-id revision-id provider-id user-id native-id
                created-at revision-date deleted format 
                ]} concept
        ;;Software
        software (merge {:concept-id concept-id} (remove-index-irrelevant-defaults software))
        entry-title (get-in software [:Software :EntryTitle])
        short-name (get-in software [:Software :ShortName])
        doi (get-in software [:Software :DOI :DOI])
        doi-lowercase (util/safe-lowercase doi)
        funding-source (get-in software [:Software :FundingSource])
        project-short-name (get-in software [:Software :Project :ShortName])
        project-long-name (get-in software [:Software :Project :LongName])
        publisher (get-in software [:Software :Publisher])
        citation (get-in software [:Software :Citation])
        ;;Metadata
        updated (get-in software [:Metadata :Updated])
        updated (index-util/date->elastic updated)
        language (get-in software [:Metadata :Language])
        published (get-in software [:Metadata :Published])
        published (index-util/date->elastic published)
        ;;Software Description
        platform (get-in software [:SoftwareDescription :platform])
        instrument (get-in software [:SoftwareDescription :instrument])
        ;inputdataset (get-in software [:SoftwareDescription :InputDataset])
        inputvariable-name (get-in software [:SoftwareDescription :InputVariable :Name])
        ;outputdataset (get-in software [:SoftwareDescription :OutputDataset])
        outputvariable-name (get-in software [:SoftwareDescription :OutputVariable :Name])
        interoperable-software-name (get-in software [:SoftwareDescription :InteroperableSoftware :Name])
        interoperable-software-workflow-name (get-in software [:SoftwareDescription :InteroperableSoftware :Workflow :Name])
        ;;Execute
        license-name (get-in software [:Execute :Access :LicenseURL :Name])
        constraints (get-in software [:Execute :Access :Constraints])
        install-documentation (get-in software [:Execute :Install :Documentation])
        install-language (get-in software [:Execute :Install :Language])
        install-version (get-in software [:Execute :Install :Language :Version])
        install-memory (get-in software [:Execute :Install :Memory])
        install-operatingsystem (get-in software [:Execute :Install :OperatingSystem])
        install-time (get-in software [:Execute :Install :InstallationTime])
        install-dependency (get-in software [:Execute :Install :Dependencies])
        ;;SoftwareUsage
        softwareusage-type (get-in software [:SoftwareUsage :Type])
        ;;Version
        version-id (get-in software [:Version :Version])
        parsed-version-id (collection-util/parse-version-id version-id) ;; use the parse tool of collection concept
        release-date (get-in software [:Version :ReleaseDate])
        release-date (index-util/date->elastic release-date)
        supersedes (get-in software [:Version :Supersedes])
        superseded-by (get-in software [:Version :SupersededBy])
        ;;Contact
        contact-person-role (get-in software [:Contact :ContactPerson :Roles])
        contact-person-firstname (get-in software [:Contact :ContactPerson :FirstName])
        contact-person-lastname (get-in software [:Contact :ContactPerson :LastName])
        contact-person-middlename (get-in software [:Contact :ContactPerson :MiddleName])

        
        contact-group-role (get-in software [:Contact :ContactGroup :Roles])
        contact-group-name (get-in software [:Contact :ContactGroup :GroupName])
        ;;ScienceKeywords
        ]

    (merge {:concept-id concept-id
            :doi-stored doi
            :revision-id revision-id
            :concept-seq-id (:sequence-number (concepts/parse-concept-id concept-id))
            :native-id native-id
            :native-id-lowercase (util/safe-lowercase native-id)
            :user-id user-id
            :deleted (boolean deleted)
            :revision-date2 revision-date
            :provider-id provider-id
            :provider-id-lowercase (util/safe-lowercase provider-id)
            :metadata-format (name (mt/format-key format))
            :created-at created-at
            
            ;;Software
            :entry-title (safe-trim entry-title)
            :entry-title-lowercase (safe-trim (util/safe-lowercase entry-title))
            :short-name short-name
            :short-name-lowercase (util/safe-lowercase short-name)
            :doi doi
            :doi-lowercase doi-lowercase
            :funding-source (safe-trim funding-source)
            :project-short-name (safe-trim project-short-name)
            :project-short-name-lowercase (safe-trim (util/safe-lowercase project-short-name))
            :project-long-name (safe-trim project-long-name)
            :project-long-name-lowercase (safe-trim (util/safe-lowercase project-long-name))
            :publisher (safe-trim publisher)
            :publisher-lowercase (safe-trim (util/safe-lowercase publisher))
            :citation citation
            ;;Metadata
            :update-time updated
            :language (safe-trim language)
            :language-lowercase (safe-trim (util/safe-lowercase language))
            :publish-time published
            ;;Software Description
            :platform (safe-trim platform)
            :platform-lowercase (safe-trim (util/safe-lowercase platform))
            :instrument (safe-trim instrument)
            :instrument-lowercase (safe-trim (util/safe-lowercase instrument))
            ;:inputdataset inputdataset 
            :inputvariable-name (safe-trim inputvariable-name)
            ;:outputdataset outputdataset 
            :outputvariable-name (safe-trim outputvariable-name)
            :interoperable-software-name (safe-trim interoperable-software-name)
            :interoperable-software-workflow-name (safe-trim interoperable-software-workflow-name)
            ;;Execute
            :license-name (safe-trim license-name)  
            :license-name-lowercase (safe-trim (util/safe-lowercase license-name))
            :constraints (safe-trim constraints)
            :constraints-lowercase (safe-trim (util/safe-lowercase constraints))
            :install-documentation (safe-trim install-documentation)
            :install-documentation-lowercase (safe-trim (util/safe-lowercase install-documentation))
            :install-language (safe-trim install-language)
            :install-language-lowercase (safe-trim (util/safe-lowercase install-language))
            :install-version (safe-trim install-version)
            :install-version-lowercase (safe-trim (util/safe-lowercase install-version))
            :install-memory (safe-trim install-memory)
            :install-memory-lowercase (safe-trim (util/safe-lowercase install-memory))
            :install-operatingsystem (safe-trim install-operatingsystem)
            :install-operatingsystem-lowercase (safe-trim (util/safe-lowercase install-operatingsystem))
            :install-time (safe-trim install-time)
            :install-time-lowercase (safe-trim (util/safe-lowercase install-time))
            :install-dependency (safe-trim install-dependency)
            :install-dependency-lowercase (safe-trim (util/safe-lowercase install-dependency))
            ;;SoftwareUsage
            :softwareusage-type (safe-trim softwareusage-type)
            :softwareusage-type-lowercase (safe-trim (util/safe-lowercase softwareusage-type))
            ;;Version
            :version-id (safe-trim version-id)
            :version-id-lowercase (util/safe-lowercase version-id)
            :parsed-version-id-lowercase (util/safe-lowercase parsed-version-id)
            :release-date release-date
            :supersedes supersedes
            :superseded-by superseded-by            
            ;;Contact
            :contact-person-role (safe-trim contact-person-role)
            :contact-person-role-lowercase (safe-trim (util/safe-lowercase contact-person-role))
            :contact-person-firstname (safe-trim contact-person-firstname)
            :contact-person-firstname-lowercase (safe-trim (util/safe-lowercase contact-person-firstname))
            :contact-person-lastname (safe-trim contact-person-lastname)
            :contact-person-lastname-lowercase (safe-trim (util/safe-lowercase contact-person-lastname))
            :contact-person-middlename (safe-trim contact-person-middlename)
            :contact-person-middlename-lowercase (safe-trim (util/safe-lowercase contact-person-middlename))
            :contact-group-role (safe-trim contact-group-role)
            :contact-group-role-lowercase (safe-trim (util/safe-lowercase contact-group-role))
            :contact-group-name (safe-trim contact-group-name)
            :contact-group-name-lowercase (safe-trim (util/safe-lowercase contact-group-name))
            ;;ScienceKeywords
            :science-keywords (map #(sk/science-keyword->elastic-doc %)
                                   (:ScienceKeywords software))
            }

        )
        )
)

(defn- get-elastic-doc-for-tombstone-software
  "Get the subset of elastic field values that apply to a tombstone index operation."
  [context concept]
  (let [
        {
          {:keys [short-name version-id entry-id entry-title]} :extra-fields
          :keys [concept-id revision-id provider-id user-id
                  native-id revision-date deleted format]} concept
        ;; only used to get default ACLs for tombstones
        tombstone-umm (umm-collection/map->UMM-C {:EntryTitle entry-title})
        tombstone-umm (merge {:concept-id concept-id} tombstone-umm)
        ]
    {:concept-id concept-id
     :revision-id revision-id
     :concept-seq-id (:sequence-number (concepts/parse-concept-id concept-id))
     :native-id native-id
     :native-id-lowercase (util/safe-lowercase native-id)
     :user-id user-id
     :short-name short-name
     :short-name-lowercase (util/safe-lowercase short-name)
     :entry-id entry-id
     :entry-id-lowercase (util/safe-lowercase entry-id)
     :entry-title entry-title
     :entry-title-lowercase (util/safe-lowercase entry-title)
     :version-id version-id
     :version-id-lowercase (util/safe-lowercase version-id)
     :deleted (boolean deleted)
     :provider-id provider-id
     :provider-id-lowercase (util/safe-lowercase provider-id)
     :revision-date2 revision-date
     :metadata-format (when format (name (mt/format-key format)))
     }
    )
)

(defmethod es/parsed-concept->elastic-doc :software
  [context concept umm-software]
  (if (:deleted concept)
    (get-elastic-doc-for-tombstone-software context concept)
    (get-elastic-doc-for-full-software context
                                         concept
                                         umm-software)))
