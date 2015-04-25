(defproject interstellar "0.1.0-SNAPSHOT"
  :description "An web client for selecting only worthwhile fillums"
  :url "www.github.com/ben-biddington/interstellar"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [
  		[org.clojure/clojure "1.6.0"]
                [org.clojure/core.cache "0.6.4"]
                [org.clojure/core.memoize "0.5.6"]
  		[http-kit "2.1.16"]
  		[org.clojure/data.json "0.2.5"]
  		[enlive "1.1.5"]
                [org.clojars.pjlegato/clansi "1.3.0"]
                [org.clojure/data.xml "0.0.8"]
                [clj-time "0.9.0"]]
  :main ^:skip-aot interstellar.adapters.interstellar-cli
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :eval-in-leiningen true)
