(ns interstellar.lang-test
  (:require [clojure.test :refer :all]))

(def take-5 #(take 5 %1))

(defn example[seq]
  (take 5 (distinct seq))
  (->> seq distinct take-5))

;; #() == fn[]()
;; http://blog.fogus.me/2010/09/28/thrush-in-clojure-redux
;; http://www.braveclojure.com/writing-macros/
(deftest language-examples
  (testing "that arrow thing (threading operator)"
    (let [result (example '(0 0 1 2 3 4 5 6 7 7))]
      (is (= '(0 1 2 3 4) result))))
  
  )
