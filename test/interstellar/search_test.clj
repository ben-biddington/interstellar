(ns interstellar.search-test
  (:require [clojure.test :refer :all]
            [interstellar.search :refer :all :as search]))

(deftest that-you-get-extra-info-back
  (testing "for example"
    (let [result (search/basic 2 7.5)]
      ;(println "Results: " result)
      )))
