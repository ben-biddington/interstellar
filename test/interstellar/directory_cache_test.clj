(ns interstellar.directory-cache-test
  (:require 
   [clojure.test :refer :all]
   [interstellar.adapters.web-cache :as web-cache]))

(def cache-dir ".tmp/")

(defn around[fn]
  (.mkdir (java.io.File. cache-dir))
  (apply fn [])
  (when (nil? (System/getenv "NO-CLOBBER")) 
    (.delete (java.io.File. cache-dir))))

(use-fixtures :each around)

(defn- exists-on-disk?[file]
  (.exists (java.io.File. (str cache-dir "/" file))))

(deftest can-cache-an-earl-and-file-appears
  (testing "for example, cache a web page by its url"
    (let [url "http://example-url" body "<html />"]
      (web-cache/save cache-dir url body)
      (is (= true (exists-on-disk? (web-cache/safe-key url))) "Expeted the cache to have written to disk"))))

