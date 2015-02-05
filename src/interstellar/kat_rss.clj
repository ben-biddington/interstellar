(ns interstellar.kat-rss
  (:require
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
