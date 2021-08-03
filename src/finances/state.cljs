(ns finances.state
  (:require
    [tick.alpha.api :as tick]))

; State has predefined data to demonstrate several columns in the column chart (column per month)
; In any other cases state should be empty or loaded from persisted storage.
(defonce *state (atom [{:date (tick/date "2021-05-05") :recipient "Recipient 1" :amount 90}
                       {:date (tick/date "2021-05-15") :recipient "Recipient 2" :amount 130}
                       {:date (tick/date "2021-05-25") :recipient "Recipient 3" :amount 25}
                       {:date (tick/date "2021-06-06") :recipient "Recipient 4" :amount 50}
                       {:date (tick/date "2021-06-16") :recipient "Recipient 5" :amount 100}
                       {:date (tick/date "2021-06-26") :recipient "Recipient 6" :amount 80}
                       {:date (tick/date "2021-07-07") :recipient "Recipient 7" :amount 120}
                       {:date (tick/date "2021-07-17") :recipient "Recipient 8" :amount 30}
                       {:date (tick/date "2021-07-27") :recipient "Recipient 9" :amount 160}]))
