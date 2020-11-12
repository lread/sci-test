(ns sci-test.import
  "Utilities functions to use when importing namespace vars."
  (:require [sci.core :as sci]))

(defn fn-out-to-sci-out
  "Wrap `target-fn` binding *out* to sci's out."
  [ target-fn ]
  (fn [ & fn-args ]
    (binding [*out* @sci/out]
      (apply target-fn fn-args))))
