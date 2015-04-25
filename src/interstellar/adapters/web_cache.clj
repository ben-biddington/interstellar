(ns interstellar.adapters.web-cache
  (:import java.net.URLEncoder)
  (:require 
   [clojure.java.io :as io]
   [interstellar.adapters.directory-cache :as inner]))

(defn safe-key[text] (URLEncoder/encode text))

(defn save[cache-dir name contents]
  (inner/save cache-dir (safe-key name) contents))
