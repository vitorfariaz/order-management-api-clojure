(ns server
  (:require [diplomat.http-server :as http-server]
            [diplomat.db.orders :as db.orders]
            [ring.adapter.jetty :as ring-jetty]
            ))

(defn start []
  (db.orders/create-order {:username :anonymous-1
                           :request {:name "pizza"
                                     :size "G"}})
  (ring-jetty/run-jetty http-server/app {:port  3000
                                         :join? false}))

(defn -main
  [& args]
  (start))
