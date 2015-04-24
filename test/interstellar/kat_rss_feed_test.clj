(ns interstellar.kat-rss-feed-test
  (:require [clojure.test :refer :all]
	    [clojure.java.io :as io]
            [interstellar.kat-rss :refer :all]))

;; https://github.com/clojure-cookbook/clojure-cookbook/blob/master/04_local-io/4-22_read-write-xml.asciidoc
;; https://clojuredocs.org/clojure.core/for
;; http://gettingclojure.wikidot.com/cookbook:xml-html
(deftest finding-kat-rss-items-for-example-one-page
  (let [result (kat-rss-items 1)]
    (testing "that there are 25 items (the page size)"
      (is (= 25 (count result))))
    
    (testing "each item has seeds"
      (is (not (nil? (:seeds (first result))))))
    
    (testing "each item has an index (its position in the rss feed)"
      (is (= 1 (-> result first :index)) (format "Expected <%s> to have :index 1" (first result)))
      (is (= 25 (-> result last :index)) (format "Expected <%s> to have :index 25" (last result))))

    (testing "items are sorted by seed count, with largest first"
      (let [first (-> result first) last (-> result last)]
        (is (> (-> first :seeds) (-> last :seeds)), (format "Expected the first item's seed count <%S> to be greater than the last's <%s>" (-> first :seeds) (-> last :seeds)))
        ))))
