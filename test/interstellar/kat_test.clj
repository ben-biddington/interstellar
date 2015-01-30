(ns interstellar.kat-test
  (:use net.cgrand.enlive-html)
  (:import java.net.URL) 
  (:require [clojure.test :refer :all]
            [interstellar.core :refer :all])) 

(defn title[earl]
	(first (-> earl URL. html-resource
		(select [:title])
	)))

(defn links[earl]
	(-> earl URL. html-resource
		(select [:a])
	))

(deftest reading-web-pages
  (testing "Make a web request and select the title like this"
    (let [result (title "https://www.google.com")]
      (is (= '("Google") (get result :content)))

      ))

  (testing "Select all links like this"
    (let [result (links "https://www.google.com")]
      (is (< 0 (count result)))

      ))



	)
