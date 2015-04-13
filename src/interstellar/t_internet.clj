(ns interstellar.t-internet
  (:refer-clojure :exclude [get set])
  (:use net.cgrand.enlive-html)
  (:import java.net.URL) 
  (:import java.lang.String)
  (:import java.util.zip.GZIPInputStream)
  (:require [net.cgrand.tagsoup :as tagsoup])
  (:require [net.cgrand.jsoup :as jsoup])
  (:require [net.cgrand.xml :as xml] 
            [interstellar.cache :refer :all :as cache]))

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

(defn nice-get-gzip[earl]
  "Same as <get-gzip> but it only fetches a URL once"
  (dosync
     (let [cached (cache/get earl)]
       (when (nil? cached)
         (cache/set earl (get-gzip earl)))
       (cache/get earl))))

(defn- nice-browser-get[f]
  "Returns a function that caches the result of <f>"
  (fn[url]
    (dosync
     (let [cached (cache/get url)]
       (when (nil? cached)
         (cache/set url (apply f [url])))
       (cache/get url)))))
