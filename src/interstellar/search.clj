(ns interstellar.search
  (:require [interstellar.kat :refer :all]
            [interstellar.kat-rss :refer :all]
            [interstellar.imdb :refer :all]))

(defn- title-find[n] (reverse (sort-by :score (pmap imdb-find-by-id (take n (distinct (detail-ids)))))))

(defn- where-score-greater-than-or-equal-to[minimum]
  #(<= minimum (get % :score)))

(def ^{:private true} default-score-min 7.5)

(defn basic[count min-score]
  (filter (where-score-greater-than-or-equal-to min-score) (title-find count)))


