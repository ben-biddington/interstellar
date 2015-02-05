(ns interstellar.core-test
  (:require [clojure.test :refer :all]
            [interstellar.kat :refer :all]
            [interstellar.imdb :refer :all]))

(defn filter-by-rating-above[item, limit]
  (fn[item] true))

(defn title-find[n]
  (let [earls (detail-ids)]
    (filter (filter-by-rating-above 80) (pmap imdb-find-by-id (take n earls)))))

(defn top[]
  (title-find 1))

(defn print-list[ratings]
  (doseq [rating ratings] 
    (println (str (get rating :title) ", score: " (get rating :score) ", metascore: " (get rating :metascore)))))

(deftest end-to-end-examples
  (testing "can find the rating of the first item on the screen and print it here"
    (let [result (top)]
      (println result)
      (is (= 1 (count result)))))

  (testing "can for example, get the top 25 in the list"
    (let [result (title-find 10)]
      (print-list result)
      (is (= 10 (count result)))))
      
      )
