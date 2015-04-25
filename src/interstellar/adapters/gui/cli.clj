(ns interstellar.adapters.gui.cli
  (:refer-clojure :exclude [contains?])
  (:require 
   [clansi.core :refer :all :as c]
   [interstellar.adapters.ignore :refer :all :as s]
   [interstellar.lang :refer :all]))

(defn- legend[]
  (println "")
  (println "[" (c/style "-" :white)  "] -- seen")
  (println "[" (c/style "-" :green)  "] -- good copy")
  (println "[" (c/style "-" :red)    "] -- poor copy"))

(defn- color[rating]
  (if (s/seen? (-> rating :title))
    :white
    (if (> 8 (-> rating :kat-rating :video)) :red :green)))

(defn- format-title[t]
  (clojure.pprint/cl-format nil "~60A" t)) ;; "~75,1,1,'.A" to use dots

(defn- single[rating]
  (let [a (-> rating :kat-rating :audio) v (-> rating :kat-rating :video) s (-> rating :health :seeds) p (-> rating :health :peers) i (-> rating :index)]
    (format "[%s] -- %s %s"
      (-> rating :score) 
      (-> rating :title format-title)
      (format "(A: %s/10, V: %s/10) (index: %s) (seeds: %s, peers: %s)" a v i s p))))

(defn prn-short[ratings]
  (doseq [rating ratings] 
    (println 
     (c/style
      (single rating)
      (color rating))))

  (legend))
