(ns interstellar.cache)
  
(def -cache (atom {}))

(def clear (reset! -cache {}))

(defn has?[key] (contains? @-cache key))
