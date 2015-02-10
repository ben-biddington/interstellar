(ns interstellar.kat-rss
  (:require [clojure.xml :as xml]))

(defn- rss[earl] (xml/parse earl))

(def host "http://kickass.to")

(defn- rss-links[earl]
  (let [rss-feed (rss earl)]
     (for [n (xml-seq rss-feed) :when (= :link (:tag n))] (first (:content n)))))

(def kat-request-count (atom {:count 0}))

(defn- increment-count[]
  (swap! kat-request-count
      (fn[current-state] 
        (merge-with + current-state {:count 1}))))

(defn- kat-rss-links-page[n]
  (increment-count)
  (drop 1 (rss-links (str host "/movies/" n "/?rss=true&field=seeders&sorder=desc"))))

(defn kat-rss-links
  "Find n pages of rss links"
  [n]
  (flatten (pmap kat-rss-links-page (range 1 (+ 1 n)))))
