(ns interstellar.t-internet
  (:use net.cgrand.enlive-html)
  (:import java.net.URL) 
  (:import java.lang.String)
  (:import java.util.zip.GZIPInputStream)
  (:require [net.cgrand.tagsoup :as tagsoup])
  (:require [net.cgrand.jsoup :as jsoup])
  (:require [net.cgrand.xml :as xml]))

(def  ^{:private true} r-count (atom 0))
(defn- to-earl[text] (URL. text))
(defn request-count[] @r-count)

(defn- gzip-html-parser [stream]
  (with-open [^java.io.Closeable stream stream]
    (let [zip (GZIPInputStream. stream)]
    (jsoup/parser zip))))

(defn get-gzip[earl]
  "Fetch <earl> using gzip parser"
  (try
    (swap! r-count inc)
    (html-resource (to-earl earl) {:parser gzip-html-parser})
    (catch java.io.FileNotFoundException e {}) ;; Sometimes the urls are missing and return 404
  )
)
