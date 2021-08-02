(ns finances.utils-test
  (:require
    [cljs.test :refer-macros [deftest is testing]]
    [finances.utils :as utils]
    [tick.alpha.api :as tick]))

(deftest format-date
  (testing "Format data to 'Day Month' format"
    (is (= "1 August"
           (utils/format-date (tick/date "2021-08-01"))))))

(deftest format-amount
  (testing "Format amount to 'n ₴' format"
    (is (= "200 ₴"
           (utils/format-amount 200)))))

(deftest amount-valid?
  (testing "Check amount format is '0.00'"
    (is (= true
           (utils/amount-valid? "0.01")))
    (is (= false
           (utils/amount-valid? "0.001")))))
