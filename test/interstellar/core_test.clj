(ns interstellar.core-test
  (:require [clojure.test :refer :all]
            [interstellar.kat :refer :all]
            [interstellar.imdb :refer :all]))

(defn title-find[n]
  (let [earls (detail-ids)]
    (pmap imdb-find-by-id (take n earls))))

(defn print-list[ratings]
  (doseq [rating ratings] 
    (println (str (get rating :title) ", score: " (get rating :score) ", metascore: " (get rating :metascore)))))

(deftest end-to-end-examples
  (testing "can for example, get the top 30"
    (let [result (title-find 30)]
      (is (= 30 (count result))))))

