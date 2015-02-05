(ns interstellar.kat
  (:use net.cgrand.enlive-html)
  (:import java.net.URL) 
  (:import java.lang.String)
  (:import java.util.zip.GZIPInputStream)
  (:require [net.cgrand.tagsoup :as tagsoup])
  (:require [net.cgrand.xml :as xml]))

(defn ^{:private true} log[text] (println text))
(def ^{:private true} earl "http://kickass.so")
(def ^{:private true} movies-earl (str earl "/movies/"))
(defn ^{:private true} movies-earl-page[n] 
  (log (str "Reading page <" (str movies-earl n) ">"))
  (str movies-earl n))

(defn ^{:private true} gzip-html-parser [stream]
  (with-open [^java.io.Closeable stream stream]
    (let [zip (GZIPInputStream. stream)]
    (tagsoup/parser zip))))

(defn ^{:private true} to-earl[text] (URL. text))

(defn ^{:private true} browser-get[earl, selector]
  (select (html-resource (to-earl earl) {:parser gzip-html-parser, :headers {"Keep-Alive" 0}}) [selector]))

(defn ^{:private true} body[earl] (browser-get earl :body))
(defn ^{:private true} links[earl] 
  (log (str "Fetching links from <" earl ">"))
  (browser-get earl :a))

(defn ^{:private true} has-href-matching?[element, name]
  (let [href (get (get element :attrs) :href)]
    (if (nil? href) nil (= true (.contains href name)))))

(defn ^{:private true} has-class?[element, name] (= name (get (get element :attrs) :class)))
(defn ^{:private true} href[link] (get (get link :attrs) :href))
(defn find-links[]  
  "This one finds up to a certain number of links"
  ^{:private true}   
  (concat
   (links (movies-earl-page 1)) 
   (links (movies-earl-page 2))))

(defn ^{:private true} detail-earls[]
  (map (fn[link] (href link))
    (filter (fn[link] (has-class? link "cellMainLink")) (find-links))))

(defn ^{:private true} detail[name] (links (str earl name)))

(defn imdb-link[url]
  (let [link (filter (fn [e] (has-href-matching? e "imdb")) (detail url))]
     (get (get (first link) :attrs) :href)))
	
(defn imdb-id[url]
  (let [link (imdb-link url)]
    (first (re-find (re-matcher #"(tt[0-9]+)" link)))))
	
(defn detail-ids[]
  (let [earls (detail-earls)]
    (println earls)
    (pmap imdb-id (take 30 earls))))

