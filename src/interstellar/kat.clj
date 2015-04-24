(ns interstellar.kat
  (:refer-clojure :exclude [get set])
  (:use net.cgrand.enlive-html)
  (:import java.net.URL) 
  (:import java.lang.String)
  (:import java.util.zip.GZIPInputStream)
  (:require [net.cgrand.tagsoup :as tagsoup])
  (:require [net.cgrand.jsoup :as jsoup])
  (:require [net.cgrand.xml :as xml])
  (:require [interstellar.kat-rss :refer :all])
  (:require [interstellar.core :refer :all])
  (:require [interstellar.t-internet :refer :all :as net]))

(def  ^{:private true} debug (=(System/getenv "LOUD") "ON"))
(def  ^{:private true} request-cache (atom {}))

(defn- log[text] (when debug (println text)))
(defn web-request-count[] (net/request-count))
(defn- get[earl] (net/nice-get-gzip earl))

(defn- browser-get[earl, selector] (select (get earl) [selector]))

(defn- links        [earl] (browser-get earl :a))
(defn- spans        [earl] (browser-get earl :span))
(defn- has-class?   [element, name] (= name (get-in element [:attrs :class])))
(defn- href         [link]          (-> link :attrs :href))
(defn- how-many?[n]
  (let [page-size interstellar.kat-rss/page-size]
    (+
     (if (< 0 (rem n page-size)) 1 0)
     (max 1 (quot n page-size)))))

(defn- detail-items [n]
  "Finds <n> detail page urls by querying the rss feed"
  (kat-rss-items (how-many? n)))

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
   :audio (-> (first (spans-with-id url "audioRating")) :content first safe-parse-int)
   :video (-> (first (spans-with-id url "videoRating")) :content first safe-parse-int) })
	
(defn imdb-id[url]
  (let [link (imdb-link url)]
    (let [imdb-id (if (nil? link) nil (first-match link #"(tt[0-9]+)"))]
      imdb-id
      )))

(defn- info-for [item] {
                        :imdb-id (imdb-id (:url item)) 
                        :kat-rating (kat-rating (:url item)) 
                        :health {:seeds (:seeds item) :peers (:peers item)}})

(defn kat-info [n]
  "Gets n pages of info (imdb-id, kat-rating)"
  (pmap info-for (detail-items n)))

(defn detail-ids[]
  "Gets n pages of imdb ids"
  ;; Here, item is a kat-rss-feed-item => {:index :url :seeds :peers}
  (let [items (map #(:url %) (detail-items))] 
    (map imdb-id items))) ;; @todo -- revert to pmap again
