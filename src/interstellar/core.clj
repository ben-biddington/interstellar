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
   [interstellar.adapters.pills :refer :all :as loading]))

(defn- format-title[t]
  (clojure.pprint/cl-format nil "~75A" t)) ;; "~75,1,1,'.A" to use dots

(defn- color[rating]
  (if (s/seen? (-> rating :title))
    :white
    (if (> 8 (-> rating :kat-rating :video)) :red :green)))

(defn- single[rating]
  (let [a (-> rating :kat-rating :audio) v (-> rating :kat-rating :video)]
    (format "[%s] -- %s %s" 
      (-> rating :score) 
      (-> rating :title format-title)
      (format "(A: %s/10, V: %s/10)" a v))))

(defn- legend[]
  (println "")
  (println "[" (c/style "-" :white)  "] -- seen")
  (println "[" (c/style "-" :green)  "] -- good copy")
  (println "[" (c/style "-" :red)  "] -- poor copy"))

(defn- prn-short[ratings]
  (doseq [rating ratings] 
    (println 
     (c/style
      (single rating)
      (color rating))))

  (legend))

(defn- time-this[f]
  (let [start (t/now)]
    (let [result (apply f [])]
      { :duration (t/interval start (t/now)) :result result })))

(defn- p[cli-args]
  (let [params (cli/params cli-args)]
    [
     (if (> (count params) 0) 
       (Integer/parseInt (first params)) 
       100)
     (if (> (count params) 1) 
       (Double/parseDouble (second params)) 
       7.5)]))

(defn- no-seen[ratings] (filter #((not (s/seen? %))) ratings))

(defn- filter-by-args[ratings args]
  (if (cli/contains? args "--no-seen")
    (filter #(not (s/seen? (:title %))) ratings)
    ratings))

(defn- run[args count min-score]
  (println (format "Running search with args <%s> (will read %s items from kat.ph, and look for a minimum imdb score of %s)\n" (if (nil? args) "none" args) count min-score))
  
  (loading/start)  

  (let [timed-result (time-this #(search/basic count min-score))]
    (loading/finish)
    (let [result (filter-by-args (:result timed-result) args)]
      (prn-short result)
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
