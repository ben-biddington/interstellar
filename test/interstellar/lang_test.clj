(ns interstellar.lang-test
  (:require [clojure.test :refer :all]))

(defn example[seq]
  (take 5 (distinct seq)))

(deftest language-examples
  (testing "that arrow thing"
    (let [result (example '(0 0 1 2 3 4 5 6 7 7))]
      (is (= '(0 1 2 3 4) result))))
  
  )
