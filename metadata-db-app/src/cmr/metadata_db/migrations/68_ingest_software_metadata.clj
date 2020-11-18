(ns migrations.068-ingest-software-metadata
  (:require
   [config.mdb-migrate-helper :as h]
   [clojure.java.jdbc :as j]
   [config.migrate-config :as config]
   [clj-time.coerce :as cr]
   [cmr.common.date-time-parser :as p]
   [clojure.java.io :as io]
   [cmr.common.util :as util]))


;;(defn- insert-software-metadata-example
;;  []
;;  (let [number_id 101
;;        concept_id "W20200915-NASA_MAAP"
;;        native_id "MAAP-CMR-001"
;;        metadata (util/string->gzip-bytes "the metadata")
;;        format_type "UMM_JSON;1.0"
;;        revision_id 1
;;        revision_date "2020-09-15T00:00:00Z"
;;        deleted 0
;;        short_name "short"
;;        version_id "1"
;;        entry_id "software entry id"
;;        entry_title "Entry 1"
;;        delete_time ""
;;        user_id ""
;;        agency_name "Agency Name"
;;        transaction_id 1
;;        created_at "2020-09-15T00:00:00Z"
;;        stmt "INSERT INTO METADATA_DB.CMR_SOFTWARE (ID, CONCEPT_ID, NATIVE_ID, 
;;                METADATA, FORMAT, REVISION_ID, 
;;                REVISION_DATE, DELETED, SHORT_NAME, 
;;                VERSION_ID, ENTRY_ID, ENTRY_TITLE, 
;;                DELETE_TIME, USER_ID, AGENCY_NAME, 
;;                TRANSACTION_ID, CREATED_AT) 
;;                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
;;        sql-args [number_id, concept_id, native_id,
;;            metadata, format_type, revision_id,
;;            (when revision_date (cr/to-sql-time (p/parse-datetime revision_date))), deleted, short_name,
;;            version_id, entry_id, entry_title,      
;;            delete_time, user_id, agency_name,
;;            transaction_id, (when created_at (cr/to-sql-time (p/parse-datetime created_at))) ]]
;;    (j/db-do-prepared (config/db) stmt sql-args)))

;;(defn- insert-software-metadata-file
;;  []
;;  (let [number_id 100
;;        concept_id "W20200915-NASA_MAAP"
;;        native_id "MAAP-CMR-001"
;;        metadata (util/string->gzip-bytes (-> "example_data/software/software_example.json" io/resource slurp))
;;        format_type "UMM_JSON;1.0"
;;        revision_id 1
;;        revision_date "2020-09-15T00:00:00Z"
;;        deleted 0
;;        short_name "short"
;;        version_id "1"
;;        entry_id "software entry id"
;;        entry_title "Entry 1"
;;        delete_time ""
;;        user_id ""
;;        agency_name "Agency Name"
;;        transaction_id 1
;;        created_at "2020-09-15T00:00:00Z"
;;        stmt "INSERT INTO METADATA_DB.NASA_MAAP_SOFTWARE (ID, CONCEPT_ID, NATIVE_ID, 
;;                METADATA, FORMAT, REVISION_ID, 
;;                REVISION_DATE, DELETED, SHORT_NAME, 
;;                VERSION_ID, ENTRY_ID, ENTRY_TITLE, 
;;                DELETE_TIME, USER_ID, AGENCY_NAME, 
;;                TRANSACTION_ID, CREATED_AT) 
;;                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
;;        sql-args [number_id, concept_id, native_id,
;;            metadata, format_type, revision_id,
;;            (when revision_date (cr/to-sql-time (p/parse-datetime revision_date))), deleted, short_name,
;;            version_id, entry_id, entry_title,      
;;            delete_time, user_id, agency_name,
;;            transaction_id, (when created_at (cr/to-sql-time (p/parse-datetime created_at))) ]]
;;    (j/db-do-prepared (config/db) stmt sql-args)))

(defn- delete-software-metadata-example
  []
  (let [concept_id "W20200915-NASA_MAAP"]
    (h/sql
    (format "DELETE FROM METADATA_DB.NASA_MAAP_SOFTWARE 
            WHERE concept_id = '%s' " 
            concept_id))))

(defn up
  "Migrates the database up to version 68."
  []
  (println "migrations.068-ingest-software-metadata up...")
  ;;(insert-software-metadata-file)
  )

;;(defn up1
;;  "Migrates the database up to version 68."
;;  []
;;  (println "migrations.068-ingest-software-metadata up...")
;;  (insert-software-metadata-file))

(defn down
  "Migrates the database down from version 68."
  []
  (println "migrations.068-ingest-software-metadata down...")
  (delete-software-metadata-example))

;;(defn up-test
;;  "Testing: create a table"
;;  []
;;  (println "migrations.068-ingest-software-metadata...creating table")
;;  (h/sql "CREATE TABLE METADATA_DB.TestingTable (id INTEGER, name VARCHAR(255))"))

;;(defn down-test
;;  "Testing: drop a table"
;;  []
;;  (println "migrations.068-ingest-software-metadata...droping table")
;;  (h/sql "DROP TABLE METADATA_DB.TestingTable"))

;;(defn- insert-software-metadata-example1
;;  []
;;  (let [number_id 100
;;        concept_id "W20200915-NASA_MAAP"]
;;    (h/sql
;;    (format "INSERT INTO METADATA_DB.TestingTable (id, name) 
;;               VALUES (?, ?)" 
;;            number_id concept_id ))))

;;(defn up-test1
;;  "Migrates the database up to version 68."
;;  []
;;  (println "migrations.068-ingest-software-metadata ...insert")
;;  (insert-software-metadata-example1))