(ns interstellar.atoms-test
  (:require [clojure.test :refer :all]
            [interstellar.kat :refer :all]
            [interstellar.imdb :refer :all]))

(def an-atom
  (atom {:count 1}))

(deftest things-that-vary-over-time
  (testing "changing value over time"
    (is (= 1 (:count @an-atom))))
  )

