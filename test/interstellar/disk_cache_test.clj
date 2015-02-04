(ns interstellar.disk-cache-test
  (:require [clojure.test :refer :all]
            [interstellar.kat :refer :all] 
            [clojure.core.cache :as cache])
  )

 (def C (cache/basic-cache-factory {}))

 (defn add-to-cache[key,object]
  
  )


(deftest some-examples
  (testing "Missing item is nil"
    (is (= nil (cache/lookup C "xxx"))))

  (testing "Can put an item in and see it there"
    (let [new-cache (cache/miss C "A" "value A")]
      (is (= "value A" (cache/lookup new-cache "A")))))
  )
