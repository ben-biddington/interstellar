(ns interstellar.search
  (:require [interstellar.kat :refer :all]
            [interstellar.kat-rss :refer :all]
            [interstellar.adapters.imdb :refer :all]))

(defn- all-detail-ids[info] (map :imdb-id info))

(defn- find-kat-ratings[fillum kat-ratings]
  "Given a fillum, finds the ratings (audio, video) for it from <kat-ratings>"
  (first (filter #(= (:imdb-id fillum) (:imdb-id %)) kat-ratings)))

(defn- title-find[n] 
  (let [info (kat-info)]
    (let [imdb-ids (all-detail-ids info)]
      (let [result (reverse (sort-by :score (pmap imdb-find-by-id (take n (distinct imdb-ids)))))]
        (map #(conj % (find-kat-ratings % info)) result)
        ))))

(defn- where-score-greater-than-or-equal-to[minimum]
  #(<= minimum (get % :score)))

(def ^{:private true} default-score-min 7.5)

(defn- fail[msg & args]
  (throw (Exception. (apply format msg args))))

(defn basic[count min-score]
  (when (> min-score 10.0)
    (fail "The <min-score> cannot be greater than 10.0. (You supplied <%s>.)", min-score))

  (let [r (filter (where-score-greater-than-or-equal-to min-score) (title-find count))]
    ;; (println r)
    ;; {:imdb-id tt0816692, :title Interstellar, :metascore 74, :score 8.8, :kat-ratings TODO: ratings here}
    r
    ))

