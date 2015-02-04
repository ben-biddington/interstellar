(ns interstellar.disk-cache)

(defn save-map[map filename]
  (spit filename map))

(defn read-map[filename]
  (read-string(slurp filename)))
