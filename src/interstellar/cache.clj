(ns interstellar.cache
  (:refer-clojure :exclude [get set]))
  
(def -cache (atom {}))

(def clear (reset! -cache {}))

(defn- has?[key] (contains? @-cache key))

(defn get[key] (clojure.core/get @-cache key))

(defn set[key value] 
  (swap! -cache #(conj {key value} %)))
