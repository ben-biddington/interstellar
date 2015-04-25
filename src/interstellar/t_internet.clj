(ns interstellar.t-internet
  (:refer-clojure :exclude [get set])
  (:use net.cgrand.enlive-html)
  (:import java.net.URL) 
  (:import java.lang.String)
  (:import java.util.zip.GZIPInputStream)
  (:require [net.cgrand.tagsoup :as tagsoup])
  (:require [net.cgrand.jsoup :as jsoup])
  (:require [net.cgrand.xml :as xml])
  (:require [interstellar.adapters.web-cache :as web-cache] 
            [clojure.core.memoize :as memo]
            [clojure.data.json :as json]))

(def ^{:private true} r-count (atom 0))
(defn- to-earl[text] (URL. text))
(defn request-count[] @r-count)
(defn zero[] (reset! r-count 0))

(defn- gzip-html-parser [stream]
  (with-open [^java.io.Closeable stream stream]
    (jsoup/parser (GZIPInputStream. stream))))

(defn get-gzip[earl]
  "Fetch <earl> using gzip parser"
  (try
    (swap! r-count inc)
    (html-resource (to-earl earl) {:parser gzip-html-parser})
    (catch java.io.FileNotFoundException e {}))) ;; Sometimes the urls are missing and return 404))

(defn- memoize-to-disk [f]
  (clojure.core.memoize/build-memoizer
       #(web-cache/new-disk-web-cache)
       f
       {}))

(def ^{:private true} nice (memoize get-gzip))

(def ^{:private true} lock (Object.))

(def ^{:private true} cache-dir ".web-cache")

(def ^{:private true} disk-cache? false) ; @todo: can't get serialization working

(defn- custom-nice-get[earl]
  (locking lock
    (.mkdir (java.io.File. cache-dir))
    (let [cached (web-cache/get cache-dir earl)]
      (if (nil? cached)
        (do
          (let [fresh (get-gzip earl)]
            (web-cache/save cache-dir earl (json/write-str fresh))
            fresh))
        (json/read-str cached))
    )))

(defn nice-get-gzip[earl]
  "Same as <get-gzip> but it only fetches a URL once"
  (if disk-cache?
    (custom-nice-get earl)
    (apply nice [earl])))
