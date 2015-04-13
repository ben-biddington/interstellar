(ns interstellar.kat
  (:use net.cgrand.enlive-html)
  (:import java.net.URL) 
  (:import java.lang.String)
  (:import java.util.zip.GZIPInputStream)
  (:require [net.cgrand.tagsoup :as tagsoup])
  (:require [net.cgrand.jsoup :as jsoup])
  (:require [net.cgrand.xml :as xml])
  (:require [interstellar.kat-rss :refer :all])
  (:require [interstellar.t-internet :refer :all :as net]))

(def  ^{:private true} debug (=(System/getenv "LOUD") "ON"))
(def  ^{:private true} request-cache (atom {}))

(defn- log[text] (when debug (println text)))
(defn web-request-count[] (net/request-count))

(defn- gzip-html-parser [stream]
  (with-open [^java.io.Closeable stream stream]
    (let [zip (GZIPInputStream. stream)]
    (jsoup/parser zip))))

(defn- browser-get[earl, selector]
  (select (net/get-gzip earl) [selector]))

(defn- links        [earl] (browser-get earl :a))
(defn- spans        [earl] (browser-get earl :span))
(defn- has-class?   [element, name] (= name (get-in element [:attrs :class])))
(defn- href         [link]          (-> link :attrs :href))
(defn- detail-earls []              (kat-rss-links 5))

(defn- has-href-matching?[element, name]
  (let [href (href element)]
    (if (nil? href) false (.contains href name))))

(defn- has-id-equal-to?[element, what]
  (let [id (-> element :attrs :id)]
    (and (not (nil? id)) (= id what))))

(defn- links-with-href-matching[earl,expected] 
  (filter (fn [e] (has-href-matching? e expected)) (links earl)))

(defn- spans-with-id[earl expected] 
  (filter (fn [e] (has-id-equal-to? e expected)) (spans earl)))

(defn imdb-link[url]
  (href (first (links-with-href-matching url "imdb"))))

(defn kat-rating[url]
  "The rating as listed on the kat website (audio and video ratings by users)"
  {
   :audio (-> (first (spans-with-id url "audioRating")) :content first Integer/parseInt)
   :video (-> (first (spans-with-id url "videoRating")) :content first Integer/parseInt) })
	
(defn imdb-id[url]
  (let [link (imdb-link url)]
    (if (nil? link)
      nil
      (first (re-find (re-matcher #"(tt[0-9]+)" link))))))

(defn- info-for [url] {:imdb-id (imdb-id url) :kat-rating (kat-rating url)})

(defn kat-info []
  "Gets n pages of info (imdb-id, kat-rating)"
  (pmap info-for (detail-earls)))
	
(defn detail-ids[]
  "Gets n pages of imdb ids"
  (let [earls (detail-earls)] (pmap imdb-id earls)))

