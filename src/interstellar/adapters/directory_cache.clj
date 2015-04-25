(ns interstellar.adapters.directory-cache
  (:refer-clojure :exclude [get])
  (:require [clojure.java.io :as io]))

(defn save [cache-dir name contents]
  (spit (io/file cache-dir name) contents))

(defn get [cache-dir name]
  (slurp (io/file cache-dir name)))


