(ns interstellar.adapters.imdb
  (:refer-clojure :exclude [count])
  (:require 
   [org.httpkit.client :as http]
   [clojure.data.json :as json]
   [interstellar.lang :as core]))

(def earl "http://www.omdbapi.com" )

(defstruct fillum :imdb-id :title :metascore :score)

(def ^{:private true} count (atom 0))

(defn request-count[] @count)

(defn ^{:private true} as-double[text]
  (try
    (Double/parseDouble text)
    (catch Exception e 0.0)))

(defn ^{:private true} as-int[text]
  (try
    (Integer/parseInt text)
    (catch Exception e 0)))

(defn ^{:private true} omdb-query [opts]
  (swap! count inc)
  (let [{:keys [status headers body error]} @(http/get earl {:query-params opts :headers {"User-Agent" "ben.biddington@gmail.com"}})]
    (when-not (nil? error)
      (core/fail "The request to <%s> with params <%s> failed with an error <%s>" earl opts error))
    (let [jsontext (json/read-str body :key-fn keyword)] 
      (struct fillum (:i opts) (:Title jsontext) (as-int (:Metascore jsontext)) (as-double (:imdbRating jsontext))))))

(defn imdb-find [name]
  (omdb-query {:t name}))

(defn imdb-find-by-id [id]
  (if (nil? id)
    (struct fillum "unknown" "Unable to find because no id was provided" 0 0.0)
    (omdb-query {:i id})))

(defn imdb-find-multi-by-id 
  ([& ids](pmap imdb-find-by-id ids)))


