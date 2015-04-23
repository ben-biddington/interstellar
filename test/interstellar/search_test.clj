(ns interstellar.search-test
  (:require [clojure.test :refer :all]
            [interstellar.search :refer :all :as search]))

(deftest it-returns-a-v-quality-ratings
  (testing "for example"
    (let [result (first (search/basic 2 7.5))]
      (is (<= 0 (-> result :kat-rating :audio)), (format "Expected to find audio rating in <%s>" result))
      (is (<= 0 (-> result :kat-rating :video))) (format "Expected to find video rating in <%s>" result))))
