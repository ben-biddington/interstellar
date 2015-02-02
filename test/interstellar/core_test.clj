(ns interstellar.core-test
  (:require [clojure.test :refer :all]
            [interstellar.kat :refer :all]
            [interstellar.imdb :refer :all]))

(defn title-find[n]
  (let [earls (detail-ids)]
     (pmap imdb-find-by-id (take n earls))))

(defn top[]
  (title-find 1))

(deftest end-to-end-examples
  (testing "can find the rating of the first item on the screen and print it here"
    (let [result (top)]
      (println result)))

  (testing "can for example, get the top 10 in the list"
    (let [result (top)]
      (println (title-find 20))))

      )
