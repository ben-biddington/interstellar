(ns interstellar.imdb-test
  (:require [clojure.test :refer :all]
            [interstellar.core :refer :all]))

(defn imdb-find [name]
	{:name name}
	)

(deftest a-test
  (testing "can find robocop"
    (is (= {:name "robocop"} (imdb-find "robocop")))))
