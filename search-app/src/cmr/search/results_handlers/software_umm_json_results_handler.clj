(ns cmr.search.results-handlers.software-umm-json-results-handler
  "Handles Software umm-json results format and related functions"
  (:require
   [cheshire.core :as json]
   [cmr.common-app.services.search :as qs]
   [cmr.common-app.services.search.elastic-results-to-query-results :as elastic-results]
   [cmr.common-app.services.search.elastic-search-index :as elastic-search-index]
   [cmr.search.results-handlers.umm-json-results-helper :as results-helper]
   [cmr.search.services.query-execution.granule-counts-results-feature :as gcrf]))

(defn- software-elastic-result->meta
  "Returns a map of the meta fields for the given software elastic result."
  [elastic-result]
  (results-helper/elastic-result->meta :software elastic-result))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Software UMM JSON

(defmethod gcrf/query-results->concept-ids :umm-json-results
  [results]
  (->> results
       :items 
       (map :meta)
       (map :concept-id)))

(defmethod elastic-search-index/concept-type+result-format->fields [:software :umm-json-results]
  [concept-type query]
  results-helper/meta-fields)

(defmethod elastic-results/elastic-result->query-result-item [:software :umm-json-results]
  [context query elastic-result]
  (let [{[entry-title] :entry-title
         [entry-id] :entry-id
         [short-name] :short-name
         [version-id] :version-id} (:fields elastic-result)]
    {:meta (software-elastic-result->meta elastic-result)
     :umm {:entry-title entry-title
           :entry-id entry-id
           :short-name short-name
           :version-id version-id}}))

(defmethod results-helper/elastic-result+metadata->umm-json-item :software
  [concept-type elastic-result metadata]
  {:meta (software-elastic-result->meta elastic-result)
   :umm (json/decode metadata)})

(defmethod elastic-results/elastic-results->query-results [:software :umm-json-results]
  [context query elastic-results]
  (results-helper/query-elastic-results->query-results context :software query elastic-results))

(defmethod qs/search-results->response [:software :umm-json-results]
  [context query results]
  (json/generate-string (select-keys results [:hits :took :items])))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Software Legacy UMM JSON

(defmethod elastic-search-index/concept-type+result-format->fields [:software :legacy-umm-json]
  [concept-type query]
  (concat
   results-helper/meta-fields
   ["entry-title"
    "entry-id"
    "short-name"
    "version-id"]))

(defmethod elastic-results/elastic-result->query-result-item [:software :legacy-umm-json]
  [context query elastic-result]
  (let [{[entry-title] :entry-title
         [entry-id] :entry-id
         [short-name] :short-name
         [version-id] :version-id} (:fields elastic-result)]
    {:meta (software-elastic-result->meta elastic-result)
     :umm {:entry-title entry-title
           :entry-id entry-id
           :short-name short-name
           :version-id version-id}}))

(defmethod qs/search-results->response [:software :legacy-umm-json]
  [context query results]
  (json/generate-string (select-keys results [:hits :took :items])))
