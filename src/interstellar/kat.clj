(ns interstellar.kat
  (:use net.cgrand.enlive-html)
  (:import java.net.URL) 
  (:import java.lang.String)
  (:import java.util.zip.GZIPInputStream)
  (:require [net.cgrand.tagsoup :as tagsoup])
  (:require [net.cgrand.xml :as xml])
  (:require [interstellar.kat-rss :refer :all]))

(def  ^{:private true} debug (=(System/getenv "LOUD") "ON"))
(defn- log[text] (when debug (println text)))

(defn- gzip-html-parser [stream]
  (with-open [^java.io.Closeable stream stream]
    (let [zip (GZIPInputStream. stream)]
    (tagsoup/parser zip))))

(defn- to-earl[text] (URL. text))

(defn- get-gzip[earl]
  (try
    (html-resource (to-earl earl) {:parser gzip-html-parser})
    (catch java.io.FileNotFoundException e {}) ;; Sometimes the urls are missing and return 404
  )
)

(defn- browser-get[earl, selector]
  (select (get-gzip earl) [selector]))

(defn- links        [earl] (browser-get earl :a))
(defn- has-class?   [element, name] (= name (get-in element [:attrs :class])))
(defn- href         [link]          (get-in link [:attrs :href]))
(defn- detail-earls []              (kat-rss-links 5))

(defn- has-href-matching?[element, name]
  (let [href (href element)]
    (if (nil? href) false (.contains href name))))

(defn- links-with-href-matching[earl,expected] 
  (filter (fn [e] (has-href-matching? e expected)) (links earl)))

(defn imdb-link[url]
  (href (first (links-with-href-matching url "imdb"))))
	
(defn imdb-id[url]
  (let [link (imdb-link url)]
    (if (nil? link)
      nil
      (first (re-find (re-matcher #"(tt[0-9]+)" link))))))
	
(defn detail-ids[]
  (let [earls (detail-earls)]
    (log (str "Found <" (count earls) "> earls"))
    (pmap imdb-id earls)))

