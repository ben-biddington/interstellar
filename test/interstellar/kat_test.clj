(ns interstellar.kat-test
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
