(ns interstellar.disk-cache-test
  (:require 
   [clojure.test :refer :all]
   [interstellar.disk-cache :as disk-cache])
)

(deftest how-to-write-a-hashmap-to-a-file-on-disk
  (testing "Can write a hash to disk and read it back"
    (let [filename ".tmp"]
      (disk-cache/save (hash-map :a :value_a, :b :value_b) filename)
      (let [loaded-map (disk-cache/read filename)]
        (is (= :value_a (get loaded-map :a)))))))

