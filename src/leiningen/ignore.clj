(ns leiningen.ignore
  "[INTERSTELLAR] Ignore some movie names so they don't show up in results (perhaps you've seen them)")

(defn ignore [project & args] 
  (println "Ignoring <" args  ">"))
