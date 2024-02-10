(defproject order-management-api "0.1.0-SNAPSHOT"
  :description "Order management api"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/test.check "0.10.0"]
                 [prismatic/schema "1.1.12"]
                 [prismatic/schema-generators "0.1.3"]
                 [ring/ring-core "1.8.2"]
                 [ring/ring-jetty-adapter "1.8.2"]
                 [metosin/reitit "0.5.12"]
                 [metosin/muuntaja "0.6.8"]]
  :repl-options {:init-ns server})
