(ns interstellar.imdb-test
  (:require [clojure.test :refer :all]
            [interstellar.core :refer :all]
	    [org.httpkit.client :as http]
	    [clojure.data.json :as json]
	    ))

(defn debug[] false)

(defn imdb-find [name]
	(let [{:keys [status headers body error]} @(http/get "http://www.omdbapi.com" {:query-params {:t name}})]
		(let [jsontext (json/read-str body :key-fn keyword)] 

		(if debug (println body))

		{
			:title  	(get jsontext :Title) 
			:metascore 	(Integer/parseInt(get jsontext :Metascore))
			:score 		(Double/parseDouble(get jsontext :imdbRating))
		})))

(deftest a-test
  (testing "can find robocop"
    (let [result (imdb-find "robocop")]
      (is (= 67        (get result :metascore)))
      (is (= 7.5       (get result :score)))
      (is (= "RoboCop" (get result :title))))))
