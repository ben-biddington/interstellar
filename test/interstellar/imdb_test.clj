(ns interstellar.imdb-test
  (:require [clojure.test :refer :all]
            [interstellar.core :refer :all]
	    [org.httpkit.client :as http]
	    ))
(defn imdb-find [name]

(let [{:keys [status headers body error]} @(http/get "http://www.omdbapi.com/?t=robocop")]
	(println headers)
	(println status)
	(println body)
	(println (get body "Metascore"))
	{:name name}))

(deftest a-test
  (testing "can find robocop"
    (is (= {:name "robocop"} (imdb-find "robocop")))))
