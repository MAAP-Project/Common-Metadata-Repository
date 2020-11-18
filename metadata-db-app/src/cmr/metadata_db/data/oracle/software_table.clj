(ns cmr.metadata-db.data.oracle.software-table
  "Contains helper functions to create software table."
  (:require
   [clojure.java.jdbc :as j]))

(defmulti software-column-sql
  "Returns the sql to define provider software columns"
  (fn [provider]
    (:small provider)))

(defmethod software-column-sql false
  [provider]
  "id NUMBER,
  concept_id VARCHAR(255) NOT NULL,
  native_id VARCHAR(1030) NOT NULL,
  metadata BLOB NOT NULL,
  format VARCHAR(255) NOT NULL,
  revision_id INTEGER DEFAULT 1 NOT NULL,
  revision_date TIMESTAMP WITH TIME ZONE DEFAULT SYSTIMESTAMP NOT NULL,
  deleted INTEGER DEFAULT 0 NOT NULL,
  short_name VARCHAR(85) NOT NULL,
  version_id VARCHAR(80),
  entry_id VARCHAR(255) NOT NULL,
  entry_title VARCHAR(1030) NOT NULL,
  delete_time TIMESTAMP WITH TIME ZONE,
  user_id VARCHAR(30),
  agency_name VARCHAR(30),
  transaction_id INTEGER DEFAULT 0 NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT SYSTIMESTAMP NOT NULL")

(defmethod software-column-sql true
  [provider]
  ;; For small provider software table, there is an extra provider_id column
  (str (software-column-sql {:small false} 
        ",provider_id VARCHAR(255) NOT NULL")))

(defmulti software-constraint-sql
  "Returns the sql to define constraint on provider software table"
  (fn [provider table-name]
    (:small provider)))

(defmethod software-constraint-sql false
  [provider table-name]
  (format (str "CONSTRAINT %s_pk PRIMARY KEY (id), "

               ;; Unique constraint on native id and revision id
               "CONSTRAINT %s_con_rev
               UNIQUE (native_id, revision_id)
               USING INDEX (create unique index %s_ucr_i
               ON %s(native_id, revision_id)), "

               ;; Unique constraint on concept id and revision id
               "CONSTRAINT %s_cid_rev
               UNIQUE (concept_id, revision_id)
               USING INDEX (create unique index %s_cri
               ON %s(concept_id, revision_id))")
          table-name
          table-name
          table-name
          table-name
          table-name
          table-name
          table-name))

(defmethod software-constraint-sql true
  [provider table-name]
  (format (str "CONSTRAINT %s_pk PRIMARY KEY (id), "

            ;; Unique constraint on provider id, native id and revision id
            "CONSTRAINT %s_con_rev
            UNIQUE (provider_id, native_id, revision_id)
            USING INDEX (create unique index %s_ucr_i
            ON %s(provider_id, native_id, revision_id)), "

            ;; Unique constraint on concept id and revision id
            "CONSTRAINT %s_cid_rev
            UNIQUE (concept_id, revision_id)
            USING INDEX (create unique index %s_cri
            ON %s(concept_id, revision_id))")
          table-name
          table-name
          table-name
          table-name
          table-name
          table-name
          table-name))

(defmulti create-software-indexes
  "Create indexes on provider software table"
  (fn [db provider table-name]
    (:small provider)))

(defmethod create-software-indexes false
  [db _ table-name]
  (j/db-do-commands db (format "CREATE INDEX %s_crdi ON %s (concept_id, revision_id, deleted, delete_time)"
                               table-name
                               table-name))
  (j/db-do-commands db (format "CREATE INDEX %s_snv_i ON %s (short_name, version_id)"
                               table-name
                               table-name))
  (j/db-do-commands db (format "CREATE INDEX %s_eid_i ON %s (entry_id)"
                               table-name
                               table-name))
  (j/db-do-commands db (format "CREATE INDEX %s_et_i ON %s (entry_title)"
                               table-name
                               table-name))
  ;; Need this for transaction-id post commit check
  (j/db-do-commands db (format "CREATE INDEX %s_crtid ON %s (concept_id, revision_id, transaction_id)"
                               table-name
                               table-name))
  (j/db-do-commands db (format "CREATE INDEX %s_c_i ON %s (created_at)"
                               table-name
                               table-name)))

(defmethod create-software-indexes true
  [db _ table-name]
  (j/db-do-commands db (format "CREATE INDEX %s_crdi ON %s (concept_id, revision_id, deleted, delete_time)"
                               table-name
                               table-name))
  (j/db-do-commands db (format "CREATE INDEX %s_p_s_i ON %s (provider_id, short_name, version_id)"
                               table-name
                               table-name))
  (j/db-do-commands db (format "CREATE INDEX %s_p_ed_i ON %s (provider_id, entry_id)"
                               table-name
                               table-name))
  (j/db-do-commands db (format "CREATE INDEX %s_p_et_i ON %s (provider_id, entry_title)"
                               table-name
                               table-name))
  ;; Need this for transaction-id post commit check
  (j/db-do-commands db (format "CREATE INDEX %s_crtid ON %s (concept_id, revision_id, transaction_id)"
                                table-name
                                table-name))
  (j/db-do-commands db (format "CREATE INDEX %s_c_i ON %s (created_at)"
                               table-name)))
