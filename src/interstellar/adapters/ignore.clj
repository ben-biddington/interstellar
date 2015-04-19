(ns interstellar.adapters.ignore
  (:use clojure.java.io)
  (:require [clojure.data.json :as json]
            [interstellar.adapters.disk-cache :as c]))

(def ^{:private true} _file ".seen")

(defn- from-file[] (c/read _file))

(defn seen-list[] (from-file))

(defn seen?[name] 
  (some 
    #{(clojure.string/lower-case name)} 
    (map #(-> % clojure.string/lower-case clojure.string/trim) (seen-list))))

(defn seen[name]
  (when-not (seen? name)
    (dosync
     (let [current (from-file)]
       (let [new-value (conj current name)]
         (c/save new-value _file))))))

(defn filter-out-seen[what]
  "Filter out all the ones that have been seen"
  (filter #((not (seen? %))) what))
