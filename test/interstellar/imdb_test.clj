(ns interstellar.imdb-test
  (:require [clojure.test :refer :all]
            [interstellar.imdb :refer :all]
	    [org.httpkit.client :as http]
	    [clojure.data.json :as json]
	    ))

(deftest finding-imdb-results
  (testing "can, for example, find robocop by name"
    (let [result (imdb-find "robocop")]
      (is (= 67        (get result :metascore)))
      (is (= 7.5       (get result :score)))
      (is (= "RoboCop" (get result :title)))))

  (testing "can find by imdb id"
    (let [result (imdb-find-by-id "tt0076759")]
      (is (= "Star Wars: Episode IV - A New Hope" (get result :title))
      (is (= 8.7                                  (get result :score))))))

  (testing "can find by more than one at a time (also uses theading operatior: ->)"
    (let [result (first (imdb-find-multi-by-id "tt0076759" "tt0120907"))]
      (is (= "Star Wars: Episode IV - A New Hope" (-> :title result))
      (is (= 8.7                                  (-> :score result)))))))
