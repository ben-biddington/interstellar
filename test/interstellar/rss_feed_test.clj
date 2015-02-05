(ns interstellar.rss-feed-test
  (:require [clojure.test :refer :all]
	    [clojure.java.io :as io]
            [clojure.xml :as xml]))

(defn rss[earl]
  (xml/parse earl))

(defn rss-links[earl]
  (let [rss-feed (rss earl)]
     (for [n (xml-seq rss-feed) :when (= :link (:tag n))] (:content n))))

(defn kat-rss-links[n]
  (drop 1 (rss-links (str "http://kickass.so/movies/" n "/?rss=true&field=seeders&sorder=desc"))))

;; https://github.com/clojure-cookbook/clojure-cookbook/blob/master/04_local-io/4-22_read-write-xml.asciidoc
;; https://clojuredocs.org/clojure.core/for
;; http://gettingclojure.wikidot.com/cookbook:xml-html
(deftest finding-imdb-results
  (testing "can read all the links on an rss screen, omitting the first (because it is outside an item node)"
    (println (drop 1 (rss-links "http://kickass.so/movies/1/?rss=true&field=seeders&sorder=desc"))))

  (testing "can find say 10 pages at once"
    
    (println (drop 1 (rss-links "http://kickass.so/movies/1/?rss=true&field=seeders&sorder=desc"))))  
  )

