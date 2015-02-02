(ns interstellar.kat-test
  (:use net.cgrand.enlive-html)
  (:import java.net.URL) 
  (:import java.lang.String)
  (:import java.util.zip.GZIPInputStream)
  (:require [net.cgrand.tagsoup :as tagsoup])
  (:require [net.cgrand.xml :as xml])
  (:require [clojure.test :refer :all]
            [interstellar.kat :refer :all]))


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
