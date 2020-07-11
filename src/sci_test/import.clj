(ns sci-test.import
  "Utilities functions to use when importing namespace vars."
  (:require [sci.core :as sci]
            [clojure.walk :as walk]))

(defn- remove-sci-meta [expr]
  (walk/prewalk (fn [e]
                  (if-let [orig (meta e)]
                    (let [new (dissoc orig :line :column :end-line :end-column)
                          new (if (seq new) new nil)]
                      (with-meta e new))
                    e))
                expr))

(defn fn-out-to-sci-out
  "Wrap `target-fn` binding *out* to sci's out."
  [ target-fn ]
  (fn [ & fn-args ]
    (binding [*out* @sci/out]
      (apply target-fn fn-args))))

(defn fn-without-sci-meta-on-args
  "Wrap `target-fn` removing all sci positional metadata from call arguments."
  [ target-fn ]
  (fn [ & fn-args ]
    (remove-sci-meta (apply target-fn (map #(remove-sci-meta %) fn-args)))))
