(ns interstellar.kat-test
  (:use net.cgrand.enlive-html)
  (:import java.net.URL) 
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

(defn has-class?[element, name]
	(= name (get (get element :attrs) :class)))

(def earl "http://kickass.so")

(deftest ^:integration reading-web-pages

  (testing "Select all links like this"
    (let [result (links earl)]
      (is (< 0 (count result)))
      ))

  (testing "Select all links with css class by filtering like this"
    (let [result (filter (fn [e] (has-class? e "cellMainLink")) (links earl))]
      (is (< 0 (count result)))
      ))
)
