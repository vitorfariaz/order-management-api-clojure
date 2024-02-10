(ns server
  (:require [diplomat.http-server :as http-server]
            [ring.adapter.jetty :as ring-jetty]
            ))

(defn start []
  (ring-jetty/run-jetty http-server/app {:port  3000
                                         :join? false}))

(defn -main
  [& args]
  (start))
