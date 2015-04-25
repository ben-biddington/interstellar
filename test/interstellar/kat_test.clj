(ns interstellar.kat-test
  (:require [clojure.test :refer :all]
            [interstellar.kat :refer :all]))

(def sample-url "http://kickass.to/interstellar-2014-720p-brrip-x264-yify-t10352928.html")

(deftest ^:integration reading-web-pages
  (testing "Find an imdb link like this"
    (let [result (imdb-link sample-url)]
      (is (.contains result "imdb.com"))))

  (testing "Find an imdb identifier like this"
    (let [result (imdb-id sample-url)]
      (is (.startsWith result "tt")))))


(deftest ^:integration can-find-the-kat-rating-for-audio-and-video
  (testing "Video has a rating"
    (let [result (kat-rating sample-url)]
      (is (= 8 (:audio result)))
      (is (= 9 (:video result))))))

(deftest ^:integration can-find-say-25-items
  (let [result (kat-info 25)]
    (testing "it returns 25"
      (is (= 25 (count result))))

    (testing "and they are sorted by index, smallest first"
      (let [first (-> result first) last (-> result last)]
        (is (< (:index first) (:index last)))
        (is (= 1 (:index first)))))))

(deftest ^:integration items-are-returned-in-blocks-of-25
    (testing "asking for one returns 25. That is because 25 is the rss page size so that is the fewest you can get (one page)"
      (let [result (kat-info 1)]
        (is (= 25 (count result)))))
    
    (testing "asking for 25 returns 25"
      (let [result (kat-info 25)]
        (is (= 25 (count result)))))

    (testing "asking for 26 returns 50"
      (let [result (kat-info 26)]
        (is (= 50 (count result)))))

    (testing "asking for 51 returns 75 (and so on)"
      (let [result (kat-info 51)]
        (is (= 75 (count result))))))

