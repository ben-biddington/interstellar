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
            [clojure.core.memoize :as memo]))

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

(defn- memo-xxx [f]
  (clojure.core.memoize/build-memoizer
       #({})
       f
       {}))

(def ^{:private true} nice (memoize get-gzip))

(defn nice-get-gzip[earl]
  "Same as <get-gzip> but it only fetches a URL once"
  (apply nice [earl]))
