(ns interstellar.kat-rss
  (:require [clojure.xml :as xml]
            [interstellar.core :refer :all]))

(def ^{:private true} index (atom 0))

(defn- next-index[]
  (swap! index inc)
  @index)

(defn- reset[] (reset! index 0))

(defstruct kat-rss-feed-item :index :url :seeds :peers)

(defn- rss[earl] (xml/parse earl))

(def ^{:private true} host "http://kickass.to")

(def page-size 25)

(defn- by-tag[tags tagname]
  (filter #(= tagname (-> % :tag)) tags))

(defn- first-by-tag [tags tagname] (first (by-tag tags tagname)))

(defn- first-value-by-tag [tags tagname] 
  (let [tag (first-by-tag tags tagname)]
    (-> tag :content first)))

(defn- rss-links[n earl]
  (let [rss-feed (rss earl)]
     (for [item (xml-seq rss-feed) :when (= :item (:tag item))] 
         (struct kat-rss-feed-item
                 (next-index)
                 (first-value-by-tag (-> item :content) :link) 
                 (safe-parse-int (first-value-by-tag (-> item :content) :torrent:seeds))
                 (safe-parse-int (first-value-by-tag (-> item :content) :torrent:peers))))))

(def kat-request-count (atom {:count 0}))

(defn- increment-count[]
  (swap! kat-request-count
      (fn[current-state] 
        (merge-with + current-state {:count 1}))))

(defn- kat-rss-items-page[n]
  (increment-count)
  (rss-links n (str host "/movies/" n "/?rss=true&field=seeders&sorder=desc")))

(defn kat-rss-items
  "Find n pages of rss links"
  [n]
  (reset)
  (flatten (pmap kat-rss-items-page (range 1 (+ 1 n)))))
