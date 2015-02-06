(ns interstellar.core-test
  (:require [clojure.test :refer :all]
            [interstellar.kat :refer :all]
            [interstellar.imdb :refer :all]))

(defn title-find[n] (pmap imdb-find-by-id (take n (detail-ids)))))

(defn as-double[text]
  (try
    (Double/parseDouble text)
    (catch Exception e 0.0)))

(defn print-list[ratings]
  (doseq [rating ratings] 
    (println (str (get rating :title) ", score: " (get rating :score) ", metascore: " (get rating :metascore)))))

(defn where-score-greater-than[minimum]
  (fn [item] 
    (let [actual (as-double (get item :score))]
      (< minimum actual)))) 

(deftest end-to-end-examples
  (testing "can for example, get the top 30"
    (let [result (title-find 30)]
      (is (= 30 (count result)))))

  (testing "can filter by score (imdb rating)"
    (let [result (filter (where-score-greater-than 7.9) (title-find 50))]
      (println "The following titles have score above 7.5 on IMDB:")
      (print-list result)))
  )

