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

  (testing "Find an imdb link like this"
    (let [result (imdb-link "/wild-card-2015-hdrip-xvid-juggs-etrg-t10146153.html")]
      (is (.contains result "imdb.com"))
      ))

  (testing "Find an imdb identifier like this"
    (let [result (imdb-id "/wild-card-2015-hdrip-xvid-juggs-etrg-t10146153.html")]
      (is (= "tt2231253" result))
      ))
)
