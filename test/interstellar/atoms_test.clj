(ns interstellar.atoms-test
  (:require [clojure.test :refer :all]
            [interstellar.kat :refer :all]
            [interstellar.imdb :refer :all]))

(def an-atom
  (atom {:count 1}))

;; See: <http://www.braveclojure.com/concurrency/>
(deftest things-that-vary-over-time
  (testing "an atom is an atom"
    (is (= clojure.lang.Atom (type an-atom))))

  (testing "query its value like this"
    (is (= 1 (:count @an-atom))))

  (testing "change its value like this"
    (swap! an-atom 
      (fn[current-state] 
        (merge-with + current-state {:count 2})))

    (is (= 3 (:count @an-atom))))
  )

