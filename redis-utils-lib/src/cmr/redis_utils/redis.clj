(ns cmr.redis-utils.redis
  "Used to implement functions for calling Redis."
  (:require
   [cmr.redis-utils.config :as config]
   [taoensso.carmine :as carmine :refer [wcar]]))

(defmacro wcar*
  "Call redis with conn opts. For available commands refer to Redis docuemntation or:
  https://github.com/ptaoussanis/carmine/blob/master/src/taoensso/carmine/commands.edn"
  [& body]
  `(carmine/wcar (config/redis-conn-opts) ~@body))

(defn healthy?
  "Returns true if able to reach Redis."
  [& args]
  {:ok?
   (try
     (= "PONG" (wcar* (carmine/ping)))
     (catch Exception _
       false))})

(defn reset
  "Evict all keys in redis. Primarily for dev-system use."
  []
  (wcar* (carmine/flushall)))
