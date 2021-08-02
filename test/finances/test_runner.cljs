(ns finances.test-runner
  (:require
    [finances.transaction-test]
    [finances.utils-test]
    [figwheel.main.testing :refer [run-tests-async]]))

(defn -main [& args]
  (run-tests-async 5000))
