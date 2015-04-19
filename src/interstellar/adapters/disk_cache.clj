(ns interstellar.adapters.disk-cache
  (:require [clojure.java.io :as io])  
  (:refer-clojure :exclude [read]))

(defn- exists?[filename] (.exists (io/file filename)))
(defn- when-exists[filename fn]
  (when (exists? filename)
    (apply fn [])))

(defn save[map filename] (spit filename map))

(defn read[filename] 
  (when-exists filename #(read-string(slurp filename))))

(defn clear[filename]
  (when-exists filename
    #(io/delete-file filename)))
