(ns interstellar.imdb
  (:require 
   [org.httpkit.client :as http]
   [clojure.data.json :as json]))

(def earl "http://www.omdbapi.com" )

(defstruct fillum :title :metascore :score)

(defn omdb-query [opts]
  (let [{:keys [status headers body error]} @(http/get earl {:query-params opts :headers {"User-Agent" "ben.biddington@gmail.com"}})]
    (let [jsontext (json/read-str body :key-fn keyword)] 
      (struct fillum (get jsontext :Title) (get jsontext :Metascore) (get jsontext :imdbRating)))))

(defn imdb-find [name]
  (omdb-query {:t name}))

(defn imdb-find-by-id [id]
  (omdb-query {:i id}))

(defn imdb-find-multi-by-id 
  ([& ids](pmap imdb-find-by-id ids)))


