(ns diplomat.db.orders
  (:import (java.util UUID)))

(def orders (atom []))

(defn get-all-orders []
  @orders)

(defn create-order [order]
  (->> (str (UUID/randomUUID))
       (assoc order :id)
       (swap! orders conj)))

(defn get-orders-by-username [username]
  (get @orders username))

(defn get-order-by-id [id]
  (get @orders id))
