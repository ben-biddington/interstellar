(ns interstellar.imdb-test
  (:require [clojure.test :refer :all]
            [interstellar.core :refer :all]
	    [org.httpkit.client :as http]
	    [clojure.data.json :as json]
	    ))

(defn imdb-find [name]
	(let [{:keys [status headers body error]} @(http/get "http://www.omdbapi.com" {:query-params {:t name}})]
		(let [jsontext (json/read-str body :key-fn keyword)] 
		{
			:name  (get jsontext :Title) 
			:score (Integer/parseInt(get jsontext :Metascore))
		})))

(deftest a-test
  (testing "can find robocop"
    (is (= {:name "RoboCop" :score 67} (imdb-find "robocop")))))
