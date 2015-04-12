(ns interstellar.search
  (:require [interstellar.kat :refer :all]
            [interstellar.kat-rss :refer :all]
            [interstellar.imdb :refer :all]))

(defn- all-info[] (kat-info))

(defn- all-detail-ids[info] (map :imdb-id info))

(defn- title-find[n] 
  (let [info (kat-info)]
    (let [imdb-ids (all-detail-ids info)]
      (let [result (reverse (sort-by :score (pmap imdb-find-by-id (take n (distinct imdb-ids)))))]
        (map #(conj % {:kat-ratings "TODO: ratings here"}) result)
        ))))

(defn- where-score-greater-than-or-equal-to[minimum]
  #(<= minimum (get % :score)))

(def ^{:private true} default-score-min 7.5)

(defn basic[count min-score]
  (let [r (filter (where-score-greater-than-or-equal-to min-score) (title-find count))]
    ;(println r)
    ; {:imdb-id tt0816692, :title Interstellar, :metascore 74, :score 8.8, :kat-ratings TODO: ratings here}
    r
    ))

