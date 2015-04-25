(ns interstellar.directory-cache-test
  (:refer-clojure :exclude [:get])
  (:require 
   [clojure.test :refer :all]
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


(deftype DiskWebCache [cache-dir] ;; implement in terms of web-cache functions ;; > https://github.com/clojure/core.cache/blob/a77b003d6593f7bde2f27f03ec52310b68aa0ea6/src/main/clojure/clojure/core/cache.clj#L20
    clojure.core.cache/CacheProtocol
    
    (lookup  [cache e])
    (lookup  [cache e not-found])
    (has?    [cache e])
    (hit     [cache e])
    (miss    [cache e ret])
    (evict   [cache e])
    (seed    [cache base]))

(deftest can-use-the-memoization-plugin
  (testing "for example"
    (is (= true (has? (DiskWebCache. cache-dir) "http://fillums.org/a")))))
