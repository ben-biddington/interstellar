(ns interstellar.rss-feed-test
  (:require [clojure.test :refer :all]
	    [clojure.java.io :as io]
            [clojure.xml :as xml]
	    ))

(defn rss[earl]
  (xml/parse earl))



(deftest finding-imdb-results
  (testing "can read rss"
    (let [rss-feed (rss "http://kickass.so/movies/2/?rss=true")]
      (let [links (for [n (xml-seq rss-feed) :when (= :link (:tag n))] (:content n))]
        (println "Thesea are the links, the first one needs to go though: ")
(println links)
        )))
  )

