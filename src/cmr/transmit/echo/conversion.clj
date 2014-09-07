(ns cmr.transmit.echo.conversion
  "Contains functions for converting between CMR style and ECHO style objects"
  (:require [clojure.string :as str]
            [clojure.set :as set]
            [cmr.common.util :as util]
            [camel-snake-kebab.core :as csk]))

(defn echo-sid->cmr-sid
  "Converts an ECHO sid into a cmr style sid.

  input: {:sid {:user_authorization_type_sid {:user_authorization_type \"GUEST\"}}}
  output: :guest

  input: {:sid {:user_authorization_type_sid {:user_authorization_type \"REGISTERED\"}}}
  output: :registered

  input {:sid {:group_sid {:group_guid \"3730376E-4DCF-53EE-90ED-FE945351A64F\"}}}}
  output \"3730376E-4DCF-53EE-90ED-FE945351A64F\""
  [sid]
  (let [sid (util/map-keys->kebab-case (:sid sid))]
    (or (get-in sid [:group-sid :group-guid])
        (-> sid
            :user-authorization-type-sid
            :user-authorization-type
            str/lower-case
            keyword))))

(defn cmr-sid->echo-sid
  "Converts a cmr style sid to an ECHO sid"
  [sid]
  (if (keyword? sid)
    {:sid {:user_authorization_type_sid
           {:user_authorization_type (-> sid name str/upper-case)}}}
    {:sid {:group_sid {:group_guid sid}}}))


(defn- echo-ace->cmr-ace
  "Cleans up the access control entry of an ACL."
  [ace]
  (let [{:keys [permissions]} ace
        permissions (mapv (comp keyword str/lower-case) permissions)
        cmr-sid (echo-sid->cmr-sid ace)]
    (if (keyword? cmr-sid)
      {:permissions permissions
       :user-type cmr-sid}
      {:permissions permissions
       :group-guid cmr-sid})))

(defn cmr-ace->echo-ace
  [ace]
  (let [{:keys [permissions group-guid user-type]} ace]
    (merge {:permissions (mapv (comp str/upper-case name) permissions)}
           (cmr-sid->echo-sid (or group-guid user-type)))))

(defn- echo-coll-id->cmr-coll-id
  [cid]
  (when-let [{:keys [collection-ids restriction-flag]} cid]
    (merge {}
           (when collection-ids
             {:entry-titles (mapv :data-set-id collection-ids)})
           (when restriction-flag
             {:access-value (set/rename-keys restriction-flag
                                             {:include-undefined-value :include-undefined})}))))

(defn cmr-coll-id->echo-coll-id
  [cid]
  (when-let [{:keys [entry-titles access-value]} cid]
    (merge {}
           (when entry-titles
             {:collection-ids (for [et entry-titles]
                                {:data-set-id et})})
           (when access-value
             {:restriction-flag
              (set/rename-keys access-value
                               {:include-undefined :include-undefined-value})}))))

(defn echo-acl->cmr-acl
  "Cleans up the acl data structure to be easier to work with. See the in code comment in this namespace for an example."
  [acl]
  (-> acl
      :acl
      util/map-keys->kebab-case
      (set/rename-keys {:id :guid :access-control-entries :aces})
      (update-in [:aces] (partial mapv echo-ace->cmr-ace))
      (update-in [:catalog-item-identity :collection-identifier] echo-coll-id->cmr-coll-id)))

(defn cmr-acl->echo-acl
  "Converts a cmr style acl back to the echo style. Converting echo->cmr->echo is lossy due to
  short names and version ids not being included. These are optional and don't impact enforcement
  so it's ok."
  [acl]
  (-> acl
      (update-in [:aces] (partial mapv cmr-ace->echo-ace))
      (update-in [:catalog-item-identity :collection-identifier] cmr-coll-id->echo-coll-id)
      (set/rename-keys {:guid :id :aces :access-control-entries})
      util/map-keys->snake_case
      (#(hash-map :acl %))))