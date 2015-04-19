(ns interstellar.core
  (:gen-class)
  (:refer-clojure :exclude [contains?])
  (:require 
   [interstellar.search :refer :all :as search]
   [interstellar.kat :refer :all]
   [interstellar.kat-rss :refer :all]
   [clj-time.core :as t]
   [clansi.core :refer :all :as c]
   [interstellar.adapters.ignore :refer :all :as s]
   [interstellar.lang :refer :all]
   [interstellar.adapters.cli :refer :all :as cli]
   [interstellar.adapters.pills :refer :all :as loading]
   [interstellar.adapters.gui.cli :as gui]))

(defn- time-this[f callback]
  (let [start (t/now)]
    (let [result (apply f [])]
      (let [reply { :duration (t/interval start (t/now)) :result result }]
        (apply callback [])
        reply))))

(defn- p[cli-args]
  (let [params (cli/params cli-args)]
    [
     (if (> (count params) 0) 
       (Integer/parseInt (first params)) 
       100)
     (if (> (count params) 1) 
       (Double/parseDouble (second params)) 
       7.5)]))

(defn- filter-by-args[ratings args]
  (if (cli/contains? args "--no-seen")
    (s/filter-out-seen ratings {:field :title})
    ratings)) 

(defn- run[args count min-score]
  (println (format "Running search with args <%s> (will read %s items from kat.ph, and look for a minimum imdb score of %s)\n" (if (nil? args) "none" args) count min-score))
  
  (loading/start)  

  (let [timed-result (time-this #(search/basic count min-score) #(loading/finish))]
    (let [result (filter-by-args (:result timed-result) args)]
      (gui/prn-short result)
      (println "")
      (println (str "Required <" (:count @kat-request-count) "> rss requests to <kickass.to> and <" (str (web-request-count)) "> detail requests (page scrapes)"))
      (println (format "Duration: %ds\n" (t/in-seconds (:duration timed-result)))))))

(defn -main [& args]
  (let [params (p args)]
    (let [count (first params) min-score (second params)]
      (if (> count 200)
        (do 
          (println "Please choose a number < 200 (default = 100)")
          (System/exit 1))
        (do 
          (run args count min-score)
          (System/exit 0))))))
