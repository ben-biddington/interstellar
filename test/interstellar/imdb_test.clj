(ns interstellar.imdb-test
  (:require [clojure.test :refer :all]
            [interstellar.core :refer :all]
	    [org.httpkit.client :as http]
	    [clojure.data.json :as json]
	    ))

(def debug false)

(def earl "http://www.omdbapi.com" )

(defstruct fillum :title :metascore :score)

(defn imdb-find [name]
	(let [{:keys [status headers body error]} @(http/get earl {:query-params {:t name}})]
		(let [jsontext (json/read-str body :key-fn keyword)] 

			(if debug (println body))

			(struct fillum (get jsontext :Title) (Integer/parseInt(get jsontext :Metascore)) (Double/parseDouble(get jsontext :imdbRating)))
		)))

(defn imdb-find-by-id [id]
	(let [{:keys [status headers body error]} @(http/get earl {:query-params {:i id}})]
		(let [jsontext (json/read-str body :key-fn keyword)] 

			(if debug (println body))

			(struct fillum (get jsontext :Title) (Integer/parseInt(get jsontext :Metascore)) (Double/parseDouble(get jsontext :imdbRating)))
		)))


(deftest finding-imdb-results
  (testing "can, for example, find robocop by name"
    (let [result (imdb-find "robocop")]
      (is (= 67        (get result :metascore)))
      (is (= 7.5       (get result :score)))
      (is (= "RoboCop" (get result :title)))))

  (testing "can find by imdb id"
    (let [result (imdb-find-by-id "tt0076759")]
      (is (= "Star Wars: Episode IV - A New Hope" (get result :title))))))
