(ns interstellar.internet-test
  (:refer-clojure :exclude [get set])
  (:require [clojure.test :refer :all]
            [interstellar.t-internet :refer :all :as net]
            [interstellar.cache :refer :all :as cache]))

(defn- nice-browser-get[f]
  (fn[url]
    (dosync
     (let [cached (cache/get url)]
       (when (nil? cached)
         (cache/set url (apply f [url])))
       (cache/get url)))))

(deftest that-you-can-fetch-a-resource
  (testing "that it returns a data structure representing the page"
    (let [result (net/get-gzip "http://kickass.to/movies")]
      (is (= :html (-> result second :tag))))))

(deftest that-you-can-cache-replies
  (testing "for example, requesting the same URL twice only produces one request"
    (net/zero)
    (let [nice-get (nice-browser-get #(net/get-gzip %))]
      (dotimes [n 2]
        (apply nice-get ["http://kickass.to/movies"])))
    
    (is (= 1 (net/request-count)) "Expected exactly one request because the result ought to have been cached" )))
