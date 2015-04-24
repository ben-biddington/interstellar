(ns interstellar.core
  (:refer-clojure :exclude [contains?]))

(defn safe-parse-int[what]
  (if (clojure.string/blank? what) 
    0 
    (Integer/parseInt what)))
