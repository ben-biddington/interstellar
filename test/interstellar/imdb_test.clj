(ns interstellar.imdb-test
  (:require [clojure.test :refer :all]
            [interstellar.core :refer :all]
	    [org.httpkit.client :as http]
	    [clojure.data.json :as json]
	    ))

(def debug false)

(defstruct fillum :title :metascore :score)

(defn imdb-find [name]
	(let [{:keys [status headers body error]} @(http/get "http://www.omdbapi.com" {:query-params {:t name}})]
		(let [jsontext (json/read-str body :key-fn keyword)] 

			(if debug (println body))

			(struct fillum (get jsontext :Title) (Integer/parseInt(get jsontext :Metascore)) (Double/parseDouble(get jsontext :imdbRating)))
		)))

(deftest finding-imdb-results
  (testing "can, for example, find robocop"
    (let [result (imdb-find "robocop")]
      (is (= 67        (get result :metascore)))
      (is (= 7.5       (get result :score)))
      (is (= "RoboCop" (get result :title))))))
