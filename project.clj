(defproject com/cars "0.1.0-SNAPSHOT"
  :description "TODO"
  :url "TODO"
  :license {:name "TODO: Choose a license"
            :url "http://choosealicense.com/"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.xml "0.0.8"]
                 [me.raynes/fs "1.4.6"]
                 [clj-time "0.12.0"]
                 [clj-http "3.1.0"]
                 [cheshire "5.6.3"]
                 [hiccup "1.0.5"]
                 [mount "0.1.10"]
                 [liberator "0.13"]
                 [ring "1.5.0"]
                 [compojure "1.5.1"]
                 [clj-tagsoup/clj-tagsoup "0.3.0" :exclusions [org.clojure/clojure]]
                 [enlive "1.1.6"]]
  :ring {:handler cars.api/handler
         :nrepl {:start? true}}
  :plugins [[lein-ring "0.9.7"]]
  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.11"]]
                   :source-paths ["dev"]}})
