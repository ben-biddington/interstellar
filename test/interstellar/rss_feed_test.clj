(ns interstellar.rss-feed-test
  (:require [clojure.test :refer :all]
	    [clojure.java.io :as io]
            [clojure.xml :as xml]
	    ))

(defn rss[earl]
  (xml/parse earl)
)


(deftest finding-imdb-results
  (testing "can read rss"
    (let [rss-feed (rss "http://kickass.so/movies/2/?rss=true")]
      (println (for [n (xml-seq rss-feed) :when (= :item (:tag n))] (:content n)))))
  )

