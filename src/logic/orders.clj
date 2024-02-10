(ns logic.orders
  (:require [schema.core :as s])
  (:import (clojure.lang Delay)
           (java.time Duration LocalDateTime)))

(defn created-before-20-minutes
  [{:keys [timestamp]}
   now]
  (.isBefore timestamp (.plus now (Duration/ofMinutes 20))))

(defn some-created-before-20-minutes
  [customer-orders now]
  (some #(created-before-20-minutes now %) customer-orders))

(s/defn create-order
  [customer-orders
   create-order :- Delay
   now :- LocalDateTime]
  (if (some-created-before-20-minutes customer-orders now)
    customer-orders
    @create-order))
