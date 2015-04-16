(ns interstellar.core
  (:gen-class)
  (:require 
   [interstellar.search :refer :all :as search]
   [interstellar.kat :refer :all]
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
(def ^{:private true} started-at (atom nil))

(defn- runtime[] (t/in-seconds (t/interval @started-at (t/now))))

(defn- start[] 
  (reset! running true)
  (reset! started-at (t/now))
  (future 
    (while @running 
      (do 
        (Thread/sleep 100) 
        (print ".") (swap! pill-count inc)
        (when (= 0 (mod @pill-count 50))
          (print (format " -- [%ss]\n" (runtime))))
        (flush)))))
  
(defn- finish[] 
  (reset! running false)
  (print "\n\n") (flush))  

(defn- p[cli-args]
  [
   (if (> (count cli-args) 0) 
     (Integer/parseInt (first cli-args)) 
     100)
   (if (> (count cli-args) 1) 
     (Double/parseDouble (second cli-args)) 
     7.5)])

(defn -main [& args]
  (let [params (p args)]
    (let [count (first params) min-score (second params)]
      (println (format "Running search with args <%s> (will read %s items from kat.ph, and look for a minimum imdb score of %s)\n" (if (nil? args) "none" args) count min-score))

      (start)  

      (let [timed-result (time-this #(search/basic count min-score))]
        (finish)
        (let [result (:result timed-result)]
          (prn-short result)
          (println "")
          (println (str "Required <" (:count @kat-request-count) "> rss requests to <kickass.to> and <" (str (web-request-count)) "> detail requests (page scrapes)"))
          (println (format "Duration: %ds" (t/in-seconds (:duration timed-result))))))))

  (System/exit 0))
