(ns interstellar.disk-cache-test
  (:require 
   [clojure.test :refer :all]
   [interstellar.adapters.disk-cache :as disk-cache]))

(def cache-file ".tmp")

(defn before[fn]
  (disk-cache/clear cache-file)
  (apply fn []))

(use-fixtures :each before)

(deftest can-write-a-hashmap-to-a-file
  (testing "for example"
    (let [filename cache-file]
      (disk-cache/save (hash-map :a :value_a, :b :value_b) cache-file)
      (let [loaded-map (disk-cache/read cache-file)]
        (is (= :value_a (get loaded-map :a)))))))

(deftest can-write-vector-to-file
  (testing "for example"
    (let [filename cache-file]
      (disk-cache/save ["Star Wars I" "Star Wars II", "Star Wars III"] cache-file)
      (let [loaded-vector (disk-cache/read cache-file)]
        (is (some #{"Star Wars I"} loaded-vector))))))

