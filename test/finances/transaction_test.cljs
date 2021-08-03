(ns finances.transaction-test
  (:require
    [cljs.test :refer-macros [deftest is testing use-fixtures]]
    [finances.state :as state]
    [finances.transaction :as transaction]
    [tick.alpha.api :as tick]))

(use-fixtures :once
              {:before (fn []
                         (reset! state/*state [{:date      (tick/date "2021-07-01")
                                                :recipient "Recipient 1"
                                                :amount    400}
                                               {:date      (tick/date "2021-08-01")
                                                :recipient "Recipient 2"
                                                :amount    100}
                                               {:date      (tick/date "2021-08-01")
                                                :recipient "Recipient 3"
                                                :amount    200}]))})

(deftest add-new!
  (testing "Add new transaction in state"
    (tick/with-clock #inst"2021-08-01"
      (is (= [{:date      (tick/date "2021-07-01")
               :recipient "Recipient 1"
               :amount    400}
              {:date      (tick/date "2021-08-01")
               :recipient "Recipient 2"
               :amount    100}
              {:date      (tick/date "2021-08-01")
               :recipient "Recipient 3"
               :amount    200}
              {:date      (tick/date "2021-08-01")
               :recipient "Recipient 4"
               :amount    30}]
             (transaction/add-new! {:recipient "Recipient 4"
                                    :amount    30}))))))

(deftest transactions->months
  (testing "Get month names from transaction date and return unique names"
    (is (= [#time/month "JULY"
            #time/month "AUGUST"]
           (transaction/transactions->months [{:date      (tick/date "2021-07-01")
                                               :recipient "Recipient 1"
                                               :amount    400}
                                              {:date      (tick/date "2021-08-01")
                                               :recipient "Recipient 2"
                                               :amount    100}
                                              {:date      (tick/date "2021-08-01")
                                               :recipient "Recipient 3"
                                               :amount    200}])))))

(deftest calculate-sums-per-month
  (testing "Get transaction sums per month"
    (is (= [400 300]
           (transaction/calculate-sums-per-month [{:date      (tick/date "2021-07-01")
                                                   :recipient "Recipient 1"
                                                   :amount    400}
                                                  {:date      (tick/date "2021-08-01")
                                                   :recipient "Recipient 2"
                                                   :amount    100}
                                                  {:date      (tick/date "2021-08-01")
                                                   :recipient "Recipient 3"
                                                   :amount    200}])))))

(deftest calculate-sum-for-month
  (testing "Get transaction sum for month"
      (is (= 300
             (transaction/calculate-sum-for-month [{:date      (tick/date "2021-07-01")
                                                    :recipient "Recipient 1"
                                                    :amount    400}
                                                   {:date      (tick/date "2021-08-01")
                                                    :recipient "Recipient 2"
                                                    :amount    100}
                                                   {:date      (tick/date "2021-08-01")
                                                    :recipient "Recipient 3"
                                                    :amount    200}]
                                                  #time/month "AUGUST")))))
