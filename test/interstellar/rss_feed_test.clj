(ns interstellar.rss-feed-test
  (:require [clojure.test :refer :all]
	    [clojure.java.io :as io]
            [clojure.xml :as xml]))

(defn rss[earl]
  (xml/parse earl))

;; https://github.com/clojure-cookbook/clojure-cookbook/blob/master/04_local-io/4-22_read-write-xml.asciidoc
;; https://clojuredocs.org/clojure.core/for
;; http://gettingclojure.wikidot.com/cookbook:xml-html
(deftest finding-imdb-results
  (testing "can read rss"
    (let [rss-feed (rss "http://kickass.so/movies/2/?rss=true")]
      (let [links (for [n (xml-seq rss-feed) :when (= :link (:tag n))] (:content n))]
        (println "These are the links, the first one needs to go though: ")
        (println links))))
  )

