(ns interstellar.imdb-test
  (:require [clojure.test :refer :all]
            [interstellar.core :refer :all]
	    [org.httpkit.client :as http]
	    [clojure.data.json :as json]
	    ))

(def debug false)

(def earl "http://www.omdbapi.com" )

(defstruct fillum :title :metascore :score)

(defn omdb-query [opts]
	(let [{:keys [status headers body error]} @(http/get earl {:query-params opts})]
		(let [jsontext (json/read-str body :key-fn keyword)] 

			(if debug (println body))

			(struct fillum (get jsontext :Title) (Integer/parseInt(get jsontext :Metascore)) (Double/parseDouble(get jsontext :imdbRating))))))

(defn imdb-find [name]
	(omdb-query {:t name}))

(defn imdb-find-by-id [id]
	(omdb-query {:i id}))

(deftest finding-imdb-results
  (testing "can, for example, find robocop by name"
    (let [result (imdb-find "robocop")]
      (is (= 67        (get result :metascore)))
      (is (= 7.5       (get result :score)))
      (is (= "RoboCop" (get result :title)))))

  (testing "can find by imdb id"
    (let [result (imdb-find-by-id "tt0076759")]
      (is (= "Star Wars: Episode IV - A New Hope" (get result :title))))))
