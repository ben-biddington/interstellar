(ns interstellar.kat-test
  (:use net.cgrand.enlive-html)
  (:import java.net.URL) 
  (:require [clojure.test :refer :all]
            [interstellar.core :refer :all]
	    [org.httpkit.client :as http]
	    [clojure.data.json :as json]))

(defn kat-search[])

(deftest finding-imdb-results
  (testing "can, for example, find robocop by name"
    (let [result (kat-search)]
      (is (= 67        (get result :metascore)))

      ))

	)
