(ns interstellar.disk-cache-test
  (:require 
   [clojure.test :refer :all]
   [interstellar.adapters.disk-cache :as disk-cache]))

(def cache-file ".tmp")

(defn before[fn]
  (disk-cache/clear cache-file))

(use-fixtures :each before)

(deftest how-to-write-a-hashmap-to-a-file-on-disk
  (testing "Can write a hash to disk and read it back"
    (let [filename cache-file]
      (disk-cache/save (hash-map :a :value_a, :b :value_b) cache-file)
      (let [loaded-map (disk-cache/read cache-file)]
        (is (= :value_a (get loaded-map :a)))))))

