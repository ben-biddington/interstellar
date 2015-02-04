(ns interstellar.disk-cache
  (:refer-clojure :exclude [read]))

(defn save[map filename]
  (spit filename map))

(defn read[filename]
  (read-string(slurp filename)))
