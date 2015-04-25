(ns interstellar.adapters.web-cache
  (:refer-clojure :exclude [get contains?])
  (:import java.net.URLEncoder)
  (:require 
   [clojure.java.io :as io]
   [interstellar.adapters.directory-cache :as inner]))

(defn safe-key[text] (URLEncoder/encode text))

(defn save[cache-dir url contents]
  (inner/save cache-dir (safe-key url) contents))

(defn contains?[cache-dir url]
  (inner/contains? cache-dir (safe-key url)))

(defn get[cache-dir url]
  (inner/get cache-dir (safe-key url)))
