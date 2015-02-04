(ns interstellar.disk-cache
  (:require [clojure.java.io :as io])  
  (:refer-clojure :exclude [read]))

(defn save[map filename]
  (spit filename map))

(defn read[filename]
  (read-string(slurp filename)))

(defn clear[filename]
  (when (.exists (io/file filename))
    (io/delete-file filename)))
