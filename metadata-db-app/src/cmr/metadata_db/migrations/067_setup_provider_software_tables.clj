(ns migrations.067-setup-provider-software-tables
  (:require [clojure.java.jdbc :as j]
            [config.migrate-config :as config]
            [config.mdb-migrate-helper :as h]
            [cmr.metadata-db.data.oracle.concept-tables :as ct]))

(defn up
  "Migrates the database up to version 67."
  []
  (println "migrations.067-setup-provider-software-tables up...")
  (let [provider1 {:provider-id "NASA_MAAP"
                  :short-name "NASA_MAAP"
                  :cmr-only false
                  :small false}
        provider2 {:provider-id "ESA_MAAP"
                  :short-name "ESA_MAAP"
                  :cmr-only false
                  :small false}]
    (ct/create-concept-table (config/db) provider1 :software)
    (ct/create-concept-table (config/db) provider2 :software))
)
;provider3 {:provider-id "NASA_ESA_MAAP"
;                  :short-name "NASA_ESA_MAAP"
;                  :cmr-only false
;                  :small false}
(defn down
  "Migrates the database down from version 67."
  []
  (println "migrations.067-setup-provider-software-tables down...")
  (let [provider1 {:provider-id "NASA_MAAP"
                  :short-name "NASA_MAAP"
                  :cmr-only false
                  :small false}
        provider2 {:provider-id "ESA_MAAP"
                  :short-name "ESA_MAAP"
                  :cmr-only false
                  :small false}]
        t1 (ct/get-table-name provider1 :software)
        t2 (ct/get-table-name provider2 :software)]
    (println (str "Table " t1))
    (h/sql (format "drop table %s" t1))
    (println (str "Table " t2))
    (h/sql (format "drop table %s" t2))
    )
)
