(ns finances.gauge-chart
  (:require
    [rum.core :as rum]
    [tick.alpha.api :as tick]
    [finances.state :as state]
    [finances.transaction :as transaction]
    [finances.utils :as utils]
    cljsjs.highcharts
    cljsjs.highcharts.highcharts-more
    cljsjs.highcharts.modules.solid-gauge))

(defn create-chart! [node value]
  (let [config {:chart       {:type :solidgauge}
                :title       nil
                :pane        {:size       "80%"
                              :startAngle -160
                              :endAngle   160
                              :background {:backgroundColor "#eeeeee"
                                           :borderWidth     0
                                           :innerRadius     "90%"
                                           :outerRadius     "100%"
                                           :shape           "arc"}}
                :yAxis       {:minColor          "#dfec80"
                              :maxColor          "#dfec80"
                              :min               0
                              :max               0
                              :lineWidth         0
                              :tickWidth         0
                              :minorTickInterval nil
                              :tickAmount        2}
                :plotOptions {:solidgauge {:dataLabels {:borderWidth 0}}}
                :series      [{:data        [value]
                               :innerRadius "90%"}]
                :tooltip     {:enabled false}
                :credits     {:enabled false}}]
    (.chart js/Highcharts node (clj->js config))))

(defn update-chart! [chart transactions]
  (let [current-month       (tick/month (tick/date))
        transaction-sum     (transaction/calculate-sum-for-month transactions current-month)
        max-transaction-sum (apply max (transaction/calculate-sums-per-month transactions))
        value               {:yAxis  {:max max-transaction-sum}
                             :series [{:data [transaction-sum]}]}]
    (.update chart (clj->js value))))

(def chart-mixin
  {:did-mount    (fn [local-state]
                   (let [chart-node      (utils/get-node! "gauge-chart")
                         current-month   (tick/month (tick/date))
                         transaction-sum (transaction/calculate-sum-for-month @state/*state current-month)
                         chart           (create-chart! chart-node
                                                        transaction-sum)]
                     (add-watch state/*state :update-gauge-chart #(update-chart! chart %4))
                     local-state))
   :will-unmount (fn [local-state]
                   (remove-watch state/*state :update-gauge-chart)
                   local-state)})

(rum/defc chart < chart-mixin []
  [:div {:id    "gauge-chart"
         :class ["col-4"]}])