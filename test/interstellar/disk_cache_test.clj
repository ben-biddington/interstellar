(ns interstellar.disk-cache-test
  (:require [clojure.test :refer :all]
            [interstellar.kat :refer :all] 
            [clojure.core.cache :as cache])
  )

 (def C (cache/basic-cache-factory {}))

 (defn add-to-cache[key,object]
  
  )


(deftest end-to-end-examples
  (testing "can cache something on disk"
    (= nil (cache/lookup C "xxx"))
    )

      )
