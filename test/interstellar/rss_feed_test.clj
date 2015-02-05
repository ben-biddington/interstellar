(ns interstellar.rss-feed-test
  (:require [clojure.test :refer :all]
	    [clojure.java.io :as io]
            [clojure.xml :as xml]))

(defn rss[earl]
  (xml/parse earl))

(defn rss-links[earl]
  (let [rss-feed (rss earl)]
     (for [n (xml-seq rss-feed) :when (= :link (:tag n))] (first (:content n)))))

(defn kat-rss-links-page[n]
  (drop 1 (rss-links (str "http://kickass.so/movies/" n "/?rss=true&field=seeders&sorder=desc"))))

(defn kat-rss-links
  "Find n pages of rss links"
  [n]
  (flatten (pmap kat-rss-links-page (range 1 4))))

;; https://github.com/clojure-cookbook/clojure-cookbook/blob/master/04_local-io/4-22_read-write-xml.asciidoc
;; https://clojuredocs.org/clojure.core/for
;; http://gettingclojure.wikidot.com/cookbook:xml-html
(deftest finding-imdb-results
  (testing "can read all the links on an rss screen, omitting the first (because it is outside an item node)"
    (is (= 25 (count (drop 1 (rss-links "http://kickass.so/movies/1/?rss=true&field=seeders&sorder=desc"))))))

  (testing "can find say the first 3 pages at once"
    (let [result (kat-rss-links 3)]
      (is (= 75 (count result)))))
)

