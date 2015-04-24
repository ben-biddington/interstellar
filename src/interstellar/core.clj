(ns interstellar.core
  (:refer-clojure :exclude [contains?]))

(defn safe-parse-int[what]
  (if (clojure.string/blank? what) 
    0 
    (Integer/parseInt what)))

(defn first-match[text pattern]
  (first (re-find (re-matcher pattern text))))
