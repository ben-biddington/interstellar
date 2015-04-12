(ns interstellar.kat-test
  (:use net.cgrand.enlive-html)
  (:import java.net.URL) 
  (:import java.lang.String)
  (:import java.util.zip.GZIPInputStream)
  (:require [net.cgrand.tagsoup :as tagsoup])
  (:require [net.cgrand.xml :as xml])
  (:require [clojure.test :refer :all]
            [interstellar.kat :refer :all]))

(def sample-url "http://kickass.to/interstellar-2014-720p-brrip-x264-yify-t10352928.html")

(deftest ^:integration reading-web-pages
  (testing "Find an imdb link like this"
    (let [result (imdb-link sample-url)]
      (is (.contains result "imdb.com"))))

  (testing "Find an imdb identifier like this"
    (let [result (imdb-id sample-url)]
      (is (.startsWith result "tt")))))

(deftest ^:integration can-find-the-kat-rating-for-audio-and-video
  (testing "Video has a rating"
    (let [result (kat-rating sample-url)]
      (is (= 8 (:audio result)))
      (is (= 9 (:video result))))))

