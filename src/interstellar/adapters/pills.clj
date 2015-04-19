(ns interstellar.adapters.pills
  (:refer-clojure :exclude [contains?])
  (:require 
   [clj-time.core :as t]
   [clansi.core :refer :all :as c]
   [interstellar.lang :refer :all]
   [interstellar.adapters.cli :refer :all :as cli]))

(def ^{:private true} pill-line-limit 75)
(def ^{:private true} pill-count (atom 0))
(def ^{:private true} running (atom false))
(def ^{:private true} started-at (atom nil))
(defn- runtime[] (t/in-seconds (t/interval @started-at (t/now))))  

(defn start[] 
  (reset! running true)
  (reset! started-at (t/now))
  (future 
    (while @running 
      (do 
        (Thread/sleep 100) 
        (print ".") (swap! pill-count inc)
        (when (= 0 (mod @pill-count pill-line-limit))
          (print (format " -- [%ss]\n" (runtime))))
        (flush)))))

(defn finish[] 
  (reset! running false)
  (flush)
  (print "\n\n") (flush))
