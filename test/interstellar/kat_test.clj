(ns interstellar.kat-test
  (:use net.cgrand.enlive-html)
  (:import java.net.URL) 
  (:require [clojure.test :refer :all]
            [interstellar.core :refer :all])) 

(defn title[earl]
	(first (-> earl URL. html-resource
		(select [:title])
	)))

(deftest reading-web-pages
  (testing "Make a web request and select the title like this"
    (let [result (title "https://www.google.com")]
      (is (= '("Google") (get result :content)))

      ))

	)
