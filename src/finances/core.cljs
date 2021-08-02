(ns ^:figwheel-hooks finances.core
  (:require
    [rum.core :as rum]
    [cljs.reader :as reader]
    [finances.state :as state]
    [finances.column-chart :as column-chart]
    [finances.gauge-chart :as gauge-chart]
    [finances.transaction :as transaction]
    [finances.utils :as utils]))

(defn extract-transaction! []
  (let [recipient (.-value (utils/get-node! "recipient"))
        amount    (reader/read-string (.-value (utils/get-node! "amount")))]
    {:recipient recipient
     :amount    amount}))

(defn clear-transaction-form! []
  (let [recipient-element (utils/get-node! "recipient")
        amount-element    (utils/get-node! "amount")]
    (set! (.-value recipient-element) "")
    (set! (.-value amount-element) "")
    (set! (.-oldValue amount-element) "")))

(defn validate-amount-field! []
  (let [old-value (or (.-oldValue (utils/get-node! "amount")) "")
        new-value (.-value (utils/get-node! "amount"))]
    (if (utils/amount-valid? new-value)
      (set! (.-oldValue (utils/get-node! "amount")) new-value)
      (set! (.-value (utils/get-node! "amount")) old-value))))

(rum/defc transaction-form []
  [:div {:class ["my-3" "p-3"]}
   [:form {:on-submit (fn [e]
                        (.preventDefault e)
                        (transaction/add-new! (extract-transaction!))
                        (clear-transaction-form!))}
    [:div {:class ["row"]}
     [:div {:class ["col"]}
      [:input {:class         ["form-control"]
               :id            "recipient"
               :type          "text"
               :placeholder   "Recipient"
               :required      true
               :pattern       ".*\\S+.*"
               :max-length    50
               :title         "This field is required"
               :auto-complete "off"}]]
     [:div {:class ["col-3"]}
      [:div {:class ["input-group"]}
       [:input {:class         ["form-control"]
                :id            "amount"
                :type          "text"
                :placeholder   "0.00"
                :required      true
                :auto-complete "off"
                :on-change     (fn [_] (validate-amount-field!))}]
       [:div {:class ["input-group-text"]}
        utils/currency-symbol]]]
     [:div {:class ["col-auto"]}
      [:button {:class ["btn btn-primary"]
                :type  "submit"}
       "Add"]]]]])

(rum/defc transaction-table < rum/reactive []
  [:div {:class ["my-3" "p-3"]}
   [:table {:class ["table"]}
    [:tbody
     (map-indexed (fn [index {:keys [date recipient amount]}]
                    (identity [:tr {:key index}
                               [:td (utils/format-date date)]
                               [:td recipient]
                               [:td {:class ["text-end"]}
                                (utils/format-amount amount)]]))
                  (reverse (rum/react state/*state)))]]])

(rum/defc charts []
  [:div {:class ["row" "my-3" "p-3"]}
   (column-chart/chart)
   (gauge-chart/chart)])

(rum/defc pane []
  [:div {:class ["container"]}
   (charts)
   (transaction-form)
   (transaction-table)])

(defn mount! []
  (when-let [app (utils/get-node! "app")]
    (rum/mount (pane) app)))

(mount!)

(defn ^:after-load on-reload []
  (mount!))