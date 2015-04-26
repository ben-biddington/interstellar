(ns interstellar.adapters.directory-cache
  (:import [java.io DataInputStream DataOutputStream])
  (:refer-clojure :exclude [get contains?])
  (:require [clojure.java.io :as io]
            [taoensso.nippy :as nippy]))

(defn save [cache-dir name contents]
  (with-open [w (io/output-stream (io/file cache-dir name))]
    (nippy/freeze-to-out! (DataOutputStream. w) contents)))

(defn contains?[cache-dir name]
  (.exists (io/file cache-dir name)))

(def ^{:private true} MISSING nil)

(defn get [cache-dir name]
  (if (contains? cache-dir name)
    (with-open [r (io/input-stream (io/file cache-dir name))]
      (nippy/thaw-from-in! (DataInputStream. r)))
    MISSING))


