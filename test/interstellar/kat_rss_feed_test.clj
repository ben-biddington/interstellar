(ns interstellar.kat-rss-feed-test
  (:require [clojure.test :refer :all]
	    [clojure.java.io :as io]
            [interstellar.kat-rss :refer :all]))

;; https://github.com/clojure-cookbook/clojure-cookbook/blob/master/04_local-io/4-22_read-write-xml.asciidoc
;; https://clojuredocs.org/clojure.core/for
;; http://gettingclojure.wikidot.com/cookbook:xml-html
(deftest finding-kat-rss-items
  (testing "can find say the first page"
    (let [result (kat-rss-items 1)]
      (is (= 25 (count result)))
      (is (not (nil? (:seeds (first result))))))))

