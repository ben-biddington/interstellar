(ns interstellar.core
  (:gen-class)
  (:require 
   [interstellar.search :refer :all :as search]
   [interstellar.kat-rss :refer :all]
   [clj-time.core :as t]))

(defn- prn-short[ratings]
  (doseq [rating ratings] 
    (println (str "\t[" (:score rating) "] -- " (:title rating)))))

(defn- time-this[f]
  (let [start (t/now)]
    (let [result (apply f [])]
      { :duration (t/interval start (t/now)) :result result })))

(defn -main [& args]
  (let [count 100 min-score 7.5]
    (println (format "Running search with args <%s>\n" (if (nil? args) "none" args)))
    (println (format "Count: <%s>, minimum imdb score: <%s>\n" count min-score))

    (let [timed-result (time-this #(search/basic count min-score))]
      (let [result (:result timed-result)]
        (prn-short result)
        (println "")
        ;(println (format "Based on asking for <%s> items from kickass.so, <%s> titles have score above %s on IMDB." count (clojure.core/count result) min-score))
        (println (str "Required <" (:count @kat-request-count) "> web requests to <kickass.to>"))
        (println (format "Duration: %ds" (t/in-seconds (:duration timed-result)))))))
  
  (System/exit 0))
