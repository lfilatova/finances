(ns finances.transaction
  (:require
    [tick.alpha.api :as tick]
    [finances.state :as state]))

(defn add-new! [{:keys [recipient amount]}]
  (swap! state/*state conj {:date      (tick/date)
                            :recipient recipient
                            :amount    amount}))

(defn- group-by-month [transactions]
  (group-by #(tick/month (:date %)) transactions))

(defn transactions->months [transactions]
  (->> transactions
       group-by-month
       keys))

(defn- calculate-sum [transactions]
  (reduce (fn [sum transaction] (+ sum (:amount transaction)))
          0
          transactions))

(defn calculate-sums-per-month [transactions]
  (->> transactions
       group-by-month
       vals
       (map calculate-sum)))

(defn calculate-sum-for-month [transactions month]
  (->> transactions
       (filter #(= (tick/month (:date %)) month))
       calculate-sum
       long))
