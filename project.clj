(defproject net-status "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [clj-http "3.12.3"]
                 [org.clojure/core.async "1.6.681"]
                 [org.clojure/tools.cli "1.0.219"]
                 [clj-commons/fs "1.6.307"]
                 [clj-commons/clj-yaml "1.0.27"]
                 [overtone/at-at "1.2.0"]]
  :repl-options {:init-ns net-status.core}
  :main net-status.core
  :aot [net-status.core]
  :profiles {:uberjar {:aot :all}})
 
