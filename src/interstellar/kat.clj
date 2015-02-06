(ns interstellar.kat
  (:use net.cgrand.enlive-html)
  (:import java.net.URL) 
  (:import java.lang.String)
  (:import java.util.zip.GZIPInputStream)
  (:require [net.cgrand.tagsoup :as tagsoup])
  (:require [net.cgrand.xml :as xml])
  (:require [interstellar.kat-rss :refer :all]))

(def  ^{:private true} debug false)
(defn ^{:private true} log[text] (when debug (println text)))
(def  ^{:private true} earl "http://kickass.so")

(defn ^{:private true} gzip-html-parser [stream]
  (with-open [^java.io.Closeable stream stream]
    (let [zip (GZIPInputStream. stream)]
    (tagsoup/parser zip))))

(defn ^{:private true} to-earl[text] (URL. text))

(defn ^{:private true} browser-get[earl, selector]
  (select (html-resource (to-earl earl) {:parser gzip-html-parser}) [selector]))

(defn ^{:private true} links[earl] 
  (log (str "Fetching links from <" earl ">"))
  (browser-get earl :a))

(defn ^{:private true} has-class?  [element, name] (= name (get-in element [:attrs :class])))
(defn ^{:private true} href        [link]          (get-in link [:attrs :href]))
(defn ^{:private true} detail-earls[]              (kat-rss-links 5))

(defn ^{:private true} has-href-matching?[element, name]
  (let [href (href element)]
    (if (nil? href) false (.contains href name))))

(defn ^{:private true} links-with-href-matching[earl,expected] 
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
    (pmap imdb-id earls)))

