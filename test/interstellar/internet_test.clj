(ns interstellar.internet-test
  (:refer-clojure :exclude [get set])
  (:require [clojure.test :refer :all]
            [interstellar.t-internet :refer :all :as net]))

(deftest that-you-can-fetch-a-resource
  (testing "that it returns a data structure representing the page"
    (let [result (net/get-gzip "http://kickass.to/movies")]
      (is (= :html (-> result second :tag))))))

(deftest that-you-can-cache-replies
  (testing "for example, requesting the same URL twice only produces one request"
    (net/zero)
    (dotimes [n 2]
      (net/nice-get-gzip "http://kickass.to/movies"))
    
    (is (= 1 (net/request-count)) "Expected exactly one request because the result ought to have been cached" )))
