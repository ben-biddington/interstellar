(ns interstellar.core
  (:gen-class)
  (:require 
   [interstellar.search :refer :all :as search]
   [interstellar.kat-rss :refer :all]
   [clj-time.core :as t]))

(defn- prn-short[ratings]
  (doseq [rating ratings] 
    (println (str " [" (:score rating) "] -- " (:title rating)))))

(defn- time-this[f]
  (let [start (t/now)]
    (let [result (apply f [])]
      { :duration (t/interval start (t/now)) :result result })))

(def ^{:private true} running (atom false))
(def ^{:private true} pill-count (atom 0))

(defn- start[] 
  (reset! running true)
  (future 
    (while @running 
      (do 
        (Thread/sleep 100) 
        (print ".") (swap! pill-count inc)
        (when (= 0 (mod @pill-count 86))
          (print "\n"))
        (flush)))))
  
(defn- finish[] 
  (reset! running false)
  (print "\n\n") (flush))  

(defn -main [& args]
  (let [count 100 min-score 7.5]
    (println (format "Running search with args <%s> (count: %s, minimum imdb score: %s)\n" (if (nil? args) "none" args) count min-score))

    (start)  

    (let [timed-result (time-this #(search/basic count min-score))]
      (finish)
      (let [result (:result timed-result)]
        (prn-short result)
        (println "")
        ;(println (format "Based on asking for <%s> items from kickass.so, <%s> titles have score above %s on IMDB." count (clojure.core/count result) min-score))
        (println (str "Required <" (:count @kat-request-count) "> web requests to <kickass.to>"))
        (println (format "Duration: %ds" (t/in-seconds (:duration timed-result)))))))

  (System/exit 0))
