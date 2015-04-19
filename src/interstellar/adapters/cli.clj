(ns interstellar.adapters.cli
  (:refer-clojure :exclude [contains?])
  (:require 
   [clansi.core :refer :all :as c]
   [interstellar.lang :refer :all]))

(defn- flag?[what] (.startsWith what "--"))

(defn contains?[args what] (some #{ what } args))

(defn params[args]
  "Returns just the parameters (minus flags)"
  (filter #(not (flag? %)) args))
