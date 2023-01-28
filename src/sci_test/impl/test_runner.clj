(ns sci-test.impl.test-runner
  (:require [sci-test.impl.clojure.test :as t]
            [sci.core :as sci]))

;; TODO: Can I not use sci internals here? It is a bit confusing and can be brittle
(defn- test-ns-vars [[ns-obj ns-vars]]
  (sci/binding [t/report-counters (ref @t/initial-report-counters)]
    (t/do-report {:type :begin-test-ns, :ns ns-obj})
    (t/test-vars ns-vars)
    (t/do-report {:type :end-test-ns, :ns ns-obj})
    @@t/report-counters))

(defn run-test-vars
  "Run all test-vars. Report on ns while running and produce summary as per clojure.test."
  [test-vars]
  (let [ns-vars (group-by (comp :ns meta) test-vars)
        summary (assoc (apply merge-with + (map test-ns-vars ns-vars))
                       :type :summary)]
    (t/do-report summary)
    summary))

(def namespaces {'sci-test.test-runner {'run-test-vars run-test-vars}})
