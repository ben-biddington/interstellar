(ns interstellar.lang)

(defn fail[message & args] 
  (throw (Exception. (apply format message args))))
