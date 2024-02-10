(ns diplomat.http-server
  (:require [controllers.order :as controllers.order]
            [reitit.ring :as ring]
            [muuntaja.core :as m]
            [reitit.ring.middleware.muuntaja :as muuntaja])
  (:gen-class))

(defn base-handler [_]
  {:status 200
   :body "on the code again"})

(defn create-order [{order :body-params}]
    {:status 200
     :body (controllers.order/create-order order)})

(defn get-users [_]
  {:status 200
   :body (controllers.order/get-all-orders)})

(defn get-order-by-id [{{:keys [id]} :path-params}]
  {:status 200
   :body (controllers.order/get-order-by-id id)})

(def app
  (ring/ring-handler
    (ring/router
      ["/"
       ["orders/:id" get-order-by-id]
       ["orders" {:get  get-users
                  :post create-order}]
       ["" base-handler]]
      {:data {:muuntaja m/instance
              :middleware [muuntaja/format-middleware]}})))
