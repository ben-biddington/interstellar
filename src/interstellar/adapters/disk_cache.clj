(ns interstellar.adapters.disk-cache
  (:require [clojure.java.io :as io])  
  (:refer-clojure :exclude [read]))

(def ^{:private true} lock (Object.))

(defn- exists?[filename] 
  (locking lock (.exists (io/file filename))))

(defn- when-exists[filename fn]
  (when (exists? filename)
    (apply fn [])))

(defn save[map filename] 
  (locking lock (spit filename map)))

(defn read[filename] 
  (locking lock (when-exists filename #(read-string(slurp filename)))))

(defn clear[filename]
  (locking lock
  (when-exists filename
    #(io/delete-file filename))))
