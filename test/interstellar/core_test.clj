(ns interstellar.core-test
  (:require [clojure.test :refer :all]
            [interstellar.kat :refer :all]
            [interstellar.kat-rss :refer :all]
            [interstellar.imdb :refer :all]))

(defn title-find[n] (reverse (sort-by :score (pmap imdb-find-by-id (take n (distinct (detail-ids)))))))

(defn print-list[ratings]
  (doseq [rating ratings] 
    (println (str (get rating :title) ", score: " (get rating :score) ", metascore: " (get rating :metascore)))))

(defn prn-short[ratings]
  (doseq [rating ratings] 
    (println (str "[" (get rating :score) "] -- " (get rating :title)))))

(defn where-score-greater-than-or-equal-to[minimum]
  (fn [item] 
    (<= minimum (get item :score)))) 

(deftest end-to-end-examples
  (testing "can for example, get the top 30"
    (let [result (title-find 30)]
      (is (= 30 (count result)))))

  (testing "can filter by score (imdb rating)"
    (let [expected 250 score-min 8.0]
      (let [result (filter (where-score-greater-than-or-equal-to score-min) (title-find expected))]
        (println (str "Based on asking for <" expected "> items from kickass.so, the following <" (count result) "> titles have score above " score-min " on IMDB:\n"))
        (prn-short result)
        (println (str "\nAnd that required <" (:count @kat-request-count) "> web requests to <kickass.to>")))))
  )

