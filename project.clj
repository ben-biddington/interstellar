(defproject interstellar "0.1.0-SNAPSHOT"
  :description "An web client for selecting only worthwhile fillums"
  :url "www.github.com/ben-biddington/interstellar"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [
  		[org.clojure/clojure "1.6.0"]
                [org.clojure/core.cache "0.6.4"]
  		[http-kit "2.1.16"]
  		[org.clojure/data.json "0.2.5"]
  		[enlive "1.1.5"]]
  :main ^:skip-aot interstellar.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
