(ns interstellar.disk-cache-test
  (:require [clojure.test :refer :all]
            [interstellar.kat :refer :all] 
            [clojure.core.cache :as cache])
  )

(def C (cache/basic-cache-factory {}))

(deftest some-examples
  (testing "Missing item is nil"
    (is (= nil (cache/lookup C "xxx"))))

  (testing "Can put an item in and see it there"
    (let [new-cache (cache/miss C "A" "value A")]
      (is (= "value A" (cache/lookup new-cache "A")))))
  )

(defn save-map[map filename]
  (spit filename map))

(defn read-map[filename]
  (read-string(slurp filename)))

(deftest how-to-write-a-hashmap-to-a-file-on-disk
  (testing "Can write a hash to disk and read it back"
    (let [filename ".tmp"]
      (save-map (hash-map :a :value_a, :b :value_b) filename)
      (let [loaded-map (read-map filename)]
        (is (= :value_a (get loaded-map :a)))))))

