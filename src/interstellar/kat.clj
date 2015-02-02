(ns interstellar.kat
  (:use net.cgrand.enlive-html)
  (:import java.net.URL) 
  (:import java.lang.String)
  (:import java.util.zip.GZIPInputStream)
  (:require [net.cgrand.tagsoup :as tagsoup])
  (:require [net.cgrand.xml :as xml]))

(defn gzip-html-parser [stream]
  (with-open [^java.io.Closeable stream stream]
    (let [zip (GZIPInputStream. stream)]
    (tagsoup/parser zip))))

(defn to-earl[text]
	(URL. text))

(defn browser-get[earl,selector]
	(select (html-resource (to-earl earl) {:parser gzip-html-parser }) [selector]))

(defn title[earl]
	(browser-get earl :title))

(defn body[earl]
	(browser-get earl :body))

(defn links[earl]
	(browser-get earl :a))

(defn has-href-matching?[element, name]
  (let [href (get (get element :attrs) :href)]
    (if (nil? href) nil (= true (.contains href name)))))

(defn has-class?[element, name]
	(= name (get (get element :attrs) :class)))

(def ^{:private true} earl "http://kickass.so")

(defn ^{:private true} href[link]
	(get (get link :attrs) :href))

(defn ^{:private true} detail-earls[]
  (map (fn[link] (href link))
    (filter (fn[link] (has-class? link "cellMainLink")) (links earl))))

(defn detail[name]
	(links (str earl name)))

(defn imdb-link[url]
  (let [link (filter (fn [e] (has-href-matching? e "imdb")) (detail url))]
     (get (get (first link) :attrs) :href)))
	
(defn imdb-id[url]
  (let [link (imdb-link url)]
    (first (re-find (re-matcher #"(tt[0-9]+)" link)))))
	
(defn detail-ids[]
  (map imdb-id (detail-earls)))

