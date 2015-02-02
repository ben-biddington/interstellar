(ns interstellar.core-test
  (:require [clojure.test :refer :all]
            [interstellar.kat :refer :all]
            [interstellar.imdb :refer :all]))

(defn top[]
  (imdb-id (first (detail-earls))))

(deftest end-to-end-examples
  (testing "can find the rating of the first item on the screen and print it here"
    (let [result (top)]
      (println (imdb-find-by-id result))))

      )
