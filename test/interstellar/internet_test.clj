(ns interstellar.internet-test
  (:require [clojure.test :refer :all]
            [interstellar.t-internet :refer :all :as net]
            [interstellar.cache :refer :all :as cache]))

(defn- browser-get[earl, selector]
  (net/get-gzip (get-gzip earl) [selector]))

(defn- nice-browser-get[f cache]
  (fn[url]
    (f url)
    ))

(deftest that-you-can-fetch-a-resource
  (testing "that it returns a data structure representing the page"
    (let [result (net/get-gzip "http://kickass.to/movies")]
      (is (= :html (-> result second :tag))))))

(deftest that-you-can-cache-replies
  (testing "for example, requesting the same URL twice only produces one request"
    (net/zero)
    (net/get-gzip "http://kickass.to/movies")
    (net/get-gzip "http://kickass.to/movies")
    (is (= 1 (net/request-count)))))
