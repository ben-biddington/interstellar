(ns interstellar.directory-cache-test
  (:refer-clojure :exclude [:get])
  (:require 
   [clojure.test :refer :all]
   [clojure.core.cache :refer :all]
   [interstellar.adapters.web-cache :as web-cache]))

(def cache-dir ".tmp/")
(defn- clear[] (.delete (java.io.File. cache-dir)))

(defn around[fn]
  (clear)
  (.mkdir (java.io.File. cache-dir))
  (apply fn [])
  (when (nil? (System/getenv "NO-CLOBBER")) 
    (clear)))

(use-fixtures :each around)

(defn- exists-on-disk?[file]
  (.exists (java.io.File. (str cache-dir "/" file))))

(deftest can-cache-a-web-page-in-a-dir-on-disk
  (let [url "http://example-url" body "<html />"]
    (testing "that is produces a file on disk"
      (web-cache/save cache-dir url body)
      (is (= true (exists-on-disk? (web-cache/safe-key url))) "Expeted the cache to have written to disk"))))

(deftest it-caches-individual-resources
  (testing "for example two different resources"
    (web-cache/save cache-dir "http://fillums.org/a" "A")
    (web-cache/save cache-dir "http://fillums.org/b" "B")
    
    (let [cached-body-a (web-cache/get cache-dir "http://fillums.org/a")]
      (is (= "A" cached-body-a)))

    (let [cached-body-b (web-cache/get cache-dir "http://fillums.org/b")]
      (is (= "B" cached-body-b)))))

(deftest it-returns-nil-for-missing-resource
  (testing "for example"
    (is (nil? (web-cache/get cache-dir "http://fillums.org/404")))))

(deftest can-use-it-as-memoization-plugin-like-this
  (testing "that has? returns true when item exists"
    (web-cache/save cache-dir "http://fillums.org/examples" "example")
    (let [c (web-cache/new-disk-web-cache cache-dir)]
      (is (has? c "http://fillums.org/examples"))
      (is (not (has? c "http://fillums.org/404"))))))

(deftest lookup-returns-item-when-it-exists-otherwise-nil
  (let [c (web-cache/new-disk-web-cache cache-dir)]
    (testing "that it returns the right item"
      (web-cache/save cache-dir "http://fillums.org/a" "A")
      (web-cache/save cache-dir "http://fillums.org/b" "B")
      (let [found (lookup c "http://fillums.org/a")]
        (is (= "A" found))))

    (testing "that it returns nil otherwise"
      (let [c (web-cache/new-disk-web-cache cache-dir)]
        (let [found (lookup c "xxx-does-not-exist-xxx")]
          (is (nil? found)))))))
