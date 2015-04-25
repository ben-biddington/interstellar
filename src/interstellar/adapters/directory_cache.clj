(ns interstellar.adapters.directory-cache
  (:refer-clojure :exclude [get contains?])
  (:require [clojure.java.io :as io]))

(defn save [cache-dir name contents]
  (spit (io/file cache-dir name) contents))

(defn contains?[cache-dir name]
  (.exists (io/file cache-dir name)))

(def ^{:private true} MISSING nil)

(defn get [cache-dir name]
  (if (contains? cache-dir name)
    (slurp (io/file cache-dir name))
    MISSING))


