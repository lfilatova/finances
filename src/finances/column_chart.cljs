(ns finances.column-chart
  (:require
    [rum.core :as rum]
    [finances.state :as state]
    [finances.transaction :as transaction]
    [finances.utils :as utils]
    cljsjs.highcharts))

(defn create-chart! [node x-values y-values]
  (let [config {:chart       {:type :column}
                :title       nil
                :xAxis       {:categories x-values}
                :yAxis       {:title nil}
                :series      [{:data  y-values
                               :color "#80b4ec"}]
                :plotOptions {:column {:pointWidth 15}
                              :series {:states {:hover {:brightness 0}}}}
                :tooltip     {:enabled false}
                :legend      {:enabled false}
                :credits     {:enabled false}}]
    (.chart js/Highcharts node (clj->js config))))

(defn update-chart! [chart transactions]
  (let [months           (transaction/transactions->months transactions)
        transaction-sums (transaction/calculate-sums-per-month transactions)
        values           {:xAxis  {:categories months}
                          :series [{:data transaction-sums}]}]
    (.update chart (clj->js values))))

(def chart-mixin
  {:did-mount    (fn [local-state]
                   (let [chart-node       (utils/get-node! "column-chart")
                         months           (transaction/transactions->months @state/*state)
                         transaction-sums (transaction/calculate-sums-per-month @state/*state)
                         chart            (create-chart! chart-node
                                                         months
                                                         transaction-sums)]
                     (add-watch state/*state :update-column-chart #(update-chart! chart %4))
                     local-state))
   :will-unmount (fn [local-state]
                   (remove-watch state/*state :update-column-chart)
                   local-state)})

(rum/defc chart < chart-mixin []
  [:div {:id    "column-chart"
         :class ["col-8"]}])
