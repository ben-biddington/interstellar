(ns interstellar.adapters.directory-cache
  (:require [clojure.java.io :as io]))

(defn save [cache-dir name contents]
  (spit (io/file cache-dir name) contents))
