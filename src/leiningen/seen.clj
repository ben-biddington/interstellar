(ns leiningen.seen
  "[INTERSTELLAR] Ignore some movie names so they don't show up in results (perhaps you've seen them)"
  (:require [interstellar.adapters.ignore :as i]
            [interstellar.adapters.disk-cache :as c]))

(defn seen [project & args] 
  (when (not-empty args)
    (let [what (first args)]
      (println "Marked <" what  "> as seen.\n")
      (i/seen what)))
  
  (println (format "Here is your full seen list:\n\n%s\n" (clojure.string/join "\n" (map #(clojure.string/trim (format "* %s" %)) (i/seen-list))))))
