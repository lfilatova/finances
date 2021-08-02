(ns finances.utils
  (:require
    [tick.alpha.api :as tick]
    [tick.locale-en-us]))

(def ^:const currency-symbol "₴")

(defn get-node! [id]
  (.getElementById js/document id))

(defn format-date [date]
  (tick/format (tick/formatter "d MMMM") date))

(defn format-amount [amount]
  (str amount " " currency-symbol))

(defn amount-valid? [amount]
  (not (nil? (re-matches #"^\d*([.]?\d{0,2})?$" amount))))
