(ns interstellar.internet-test
  (:import java.net.URL) 
  (:import java.lang.String)
  (:require [clojure.test :refer :all]
            [net.cgrand.enlive-html :as net]))

(defn- to-earl[text] (URL. text))

;;(html-resource (to-earl earl))

(defn- browser-get[earl, selector]
  (net/select (get-gzip earl) [selector]))

(deftest that-you-can-fetch-a-resource
  (testing "that is returns"

    )
)
