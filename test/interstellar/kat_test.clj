(ns interstellar.kat-test
  (:use net.cgrand.enlive-html)
  (:import java.net.URL) 
  (:import java.lang.String)
  (:import java.util.zip.GZIPInputStream)
  (:require [net.cgrand.tagsoup :as tagsoup])
  (:require [net.cgrand.xml :as xml])
  (:require [clojure.test :refer :all]
            [interstellar.core :refer :all]))

(defn my-custom-xml-parser [stream]
  (with-open [^java.io.Closeable stream stream]
    (xml/parse (org.xml.sax.InputSource. stream))))

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

(def earl "http://kickass.so")

(defn detail[name]
	(links (str earl name)))

(defn imdb-link[url]
  (let [link (filter (fn [e] (has-href-matching? e "imdb")) (detail url))]
     (get (get (first link) :attrs) :href)))
	
(defn imdb-id[url]
  (let [link (imdb-link url)]
    (first (re-find (re-matcher #"(tt[0-9]+)" link)))
    ))
	
(deftest ^:integration reading-web-pages

  (testing "Select all links like this"
    (let [result (links earl)]
      (is (< 0 (count result)))
      ))

  (testing "Select all links with css class by filtering like this"
    (let [result (filter (fn [e] (has-class? e "cellMainLink")) (links earl))]
      (is (< 0 (count result)))
      ))

  (testing "Find an imdb link like this"
    (let [result (imdb-link "/wild-card-2015-hdrip-xvid-juggs-etrg-t10146153.html")]
      (is (.contains result "imdb.com"))
      ))

  (testing "Find an imdb identifier like this"
    (let [result (imdb-id "/wild-card-2015-hdrip-xvid-juggs-etrg-t10146153.html")]
      (is (= "tt2231253" result))
      ))
)
