(ns interstellar.kat-test
  (:use net.cgrand.enlive-html)
  (:import java.net.URL) 
  (:require [clojure.test :refer :all]
            [interstellar.core :refer :all]))

(defn title[earl]
	(first (-> earl URL. html-resource
		(select [:title]))))

(defn to-earl[text]
	(URL. text))

(defn body[earl]
	(select (html-resource (to-earl earl)) [:body]))

(defn links[earl]
	(select (html-resource (to-earl earl)) [:a]))

(defn has-class?[element, name]
	(= name (get (get element :attrs) :class)))

(def earl "https://www.google.com")

(deftest reading-web-pages

  (testing "Make a web request and select the body like this"
    (let [result (body earl)]
      (is (< 0 (count result)))
      ))

  (testing "Make a web request and select the title like this"
    (let [result (title earl)]
      (is (= '("Google") (get result :content)))
      ))

  (testing "Select all links like this"
    (let [result (links earl)]
      (is (< 0 (count result)))
      ))

  (testing "Make an earl like this"
    (let [result (to-earl "http://google.com")]
      (is (= java.net.URL (type result)))
      ))

  (testing "Select all links with css class by filtering like this"
    (let [result (filter (fn [e] (has-class? e "gb1")) (links earl))]
      (is (< 0 (count result)))
      ))

; The url returns gzip encoding so fail to read
  (testing "[FAILING] Can read the links from kat.ph"
    (let [result (filter (fn [e] (has-class? e "cellMainLink")) (links "http://kickass.so/movies/"))]
      (is (< 0 (count result)))
      ))

	)
