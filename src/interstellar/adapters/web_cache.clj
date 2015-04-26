(ns interstellar.adapters.web-cache
  (:refer-clojure :exclude [get contains?])
  (:import java.net.URLEncoder)
  (:require 
   [clojure.core.cache :as cache]
   [interstellar.adapters.directory-cache :as inner]))

(defn safe-key[text] (URLEncoder/encode text))

(defn save[cache-dir url contents]
  (inner/save cache-dir (safe-key url) contents))

(defn contains?[cache-dir url]
  (inner/contains? cache-dir (safe-key url)))

(defn get[cache-dir url]
  (inner/get cache-dir (safe-key url)))

; @todo: try using this in <t-internet>
(deftype DiskWebCache [cache-dir] 
  ;; => https://github.com/clojure/core.cache/blob/a77b003d6593f7bde2f27f03ec52310b68aa0ea6/src/main/clojure/clojure/core/cache.clj#L20
  clojure.core.cache/CacheProtocol
    
  (lookup  [cache e]
    (get cache-dir e))
  (lookup  [cache e not-found])
  (has?    [cache e]
    (contains? cache-dir e))
  (hit     [cache e])
  (miss    [cache e ret])
  (evict   [cache e])
  (seed    [cache base]))

(defn new-disk-web-cache[cache_dir]
  (DiskWebCache. cache_dir))
