(ns controllers.order
  (:require [diplomat.db.orders :as db.orders]
            [logic.orders :as logic.orders])
  (:import [java.time LocalDateTime]))


(defn get-order-by-id [id]
  (db.orders/get-order-by-id id))

(defn get-all-orders []
  (db.orders/get-all-orders))

(defn create-order
  [{:keys [username] :as new-order}]
  (let [customer-orders (db.orders/get-orders-by-username username)
        create-order (delay (db.orders/create-order new-order))]
    (logic.orders/create-order customer-orders create-order (LocalDateTime/now))))
