(ns interstellar.directory-cache-test
  (:require 
   [clojure.test :refer :all]
   [interstellar.adapters.directory-cache :as dir-cache]))

(def cache-dir ".tmp/")

(defn around[fn]
  (.mkdir (java.io.File. cache-dir))
  (apply fn [])
  (.delete (java.io.File. cache-dir)))

(use-fixtures :each around)

(defn- exists-on-disk?[file]
  (.exists (java.io.File. (str cache-dir "/" file))))

(deftest can-cache-an-earl-and-file-appears
  (testing "for example, cache a web page by its url"
    (dir-cache/save cache-dir "http://example-url" "<html />")
    (is (= true (exists-on-disk? "http://example-url/a-b-c")) "Expeted the cache to have written to disk")))

