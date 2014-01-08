(ns earth.driver
  (:require [vdd-core.core :as vdd]
            [vdd-core.internal.project-viz :as project-viz]
            [coffee-script.core :as coffee]
            [clojure.java.io :as io]
            [clojure.string :as s]))

(comment
  ;; want to send data over to be visualized. What could we visualize?
  ;; rings with a label
  ;; points with a label
  ;; bounding box

  {:type :ring
   :ords []
   :label "30"}

  {:type :point
   :ords []
   :label "30"}

  {:type :bounding-rectangle
   :west 0
   :north 0
   :east 0
   :south 0
   :label "30"}

  (defn point [lon lat label balloon]
    {:type :point
     :lon lon
     :lat lat
     :label label
     :balloon balloon})


  (set-viz-geometries [(point 1 1 "hi" "hello")])



)


(defn set-viz-geometries [geometries]
  (vdd/data->viz {:cmd :set-geometries
                  :geometries geometries}))

(defn add-viz-geometries [geometries]
  (vdd/data->viz {:cmd :add-geometries
                  :geometries geometries}))

(defn clear-viz-geometries []
  (vdd/data->viz {:cmd :clear-geometries}))


;; TODO consider making the coffeescript stuff part of the vdd-core framework or some kind of add on.
;; We would need a way to hook into the vdd-core events.

(defn- coffeescript-files
  "Finds all the coffeescript files in subdirectories"
  [path]
  (->> path
       io/file
       file-seq
       (map str)
       (filter #(.endsWith ^String % ".coffee"))))

(defn- compile-coffeescript-file
  "Compiles a single coffeescript file from blah.coffee to blah.js."
  [path output-path]
  (io/make-parents output-path)
  (let [js (coffee/compile-coffee (slurp path))]
    (spit output-path js)
    output-path))

(defn compile-coffeescript
  "Compiles all the coffeescript files in the visualization."
  [config]
  (let [visualization-paths (map :path (project-viz/project-visualizations config))]
    (doall (for [visualization-path visualization-paths
                 coffeescript-file (coffeescript-files visualization-path)]
             (let [compiled-path (-> coffeescript-file
                                     (s/replace visualization-path (str visualization-path "/compiled"))
                                     (s/replace ".coffee" ".js"))]
               (compile-coffeescript-file coffeescript-file compiled-path))))))
