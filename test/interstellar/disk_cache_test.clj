(ns interstellar.disk-cache-test
  (:require [clojure.test :refer :all]
            [interstellar.disk-cache :refer :all] 
            [clojure.core.cache :as cache]))

(deftest how-to-write-a-hashmap-to-a-file-on-disk
  (testing "Can write a hash to disk and read it back"
    (let [filename ".tmp"]
      (save-map (hash-map :a :value_a, :b :value_b) filename)
      (let [loaded-map (read-map filename)]
        (is (= :value_a (get loaded-map :a)))))))

