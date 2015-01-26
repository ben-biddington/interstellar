(ns interstellar.imdb-test
  (:require [clojure.test :refer :all]
            [interstellar.core :refer :all]
	    [org.httpkit.client :as http]
	    ))
(defn imdb-find [name]
(let reply  
	(http/get "http://www.omdbapi.com/?t=robocop"))
	{:name name})

(deftest a-test
  (testing "can find robocop"
    (is (= {:name "robocop"} (imdb-find "robocop")))))
