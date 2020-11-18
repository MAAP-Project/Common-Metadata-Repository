(ns cmr.indexer.data.concepts.software.science-keyword
  "Contains functions for converting science keyword domains into elastic documents"
  (:require
    [clojure.string :as str]
    [cmr.common-app.services.kms-fetcher :as kf]
    [cmr.common-app.services.kms-lookup :as kms-lookup]
    [cmr.common.util :as util]
    [cmr.umm-spec.util :as spec-util]))

(defn- normalize-sk-field-value
  "Convert science keyword field values into upper case and trim whitespace from both ends."
  [sk-field-value]
  (when (and sk-field-value (not= spec-util/not-provided sk-field-value))
    (-> sk-field-value str/trim str/upper-case)))

(defn science-keyword->elastic-doc
  "Converts a science keyword into the portion going in an elastic document."
  [science-keyword]
  (let [science-keyword-kebab-key (util/map-keys->kebab-case science-keyword)
        science-keyword-upper-case (util/map-values normalize-sk-field-value
                                                    science-keyword-kebab-key)
        {:keys [category topic term variable-level-1 variable-level-2 variable-level-3
                detailed-variable]} science-keyword-upper-case
                                                           ]
    {:category category
     :category-lowercase (util/safe-lowercase category)
     :topic topic
     :topic-lowercase (util/safe-lowercase topic)
     :term term
     :term-lowercase (util/safe-lowercase term)
     :variable-level-1 variable-level-1
     :variable-level-1-lowercase (util/safe-lowercase variable-level-1)
     :variable-level-2 variable-level-2
     :variable-level-2-lowercase (util/safe-lowercase variable-level-2)
     :variable-level-3 variable-level-3
     :variable-level-3-lowercase (util/safe-lowercase variable-level-3)
     :detailed-variable detailed-variable
     :detailed-variable-lowercase (util/safe-lowercase detailed-variable)
     }))