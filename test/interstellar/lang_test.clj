(ns interstellar.lang-test
  (:require [clojure.test :refer :all]))

(def take-5 (fn[seq] (take 5 seq)))

(defn example[seq]
  (take 5 (distinct seq))
  (-> seq distinct take-5)
  )

(deftest language-examples
  (testing "that arrow thing"
    (let [result (example '(0 0 1 2 3 4 5 6 7 7))]
      (is (= '(0 1 2 3 4) result))))
  
  )
