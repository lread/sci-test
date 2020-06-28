(ns sci-test.main
  {:no-doc true}
  (:require
   [babashka.impl.classes :as classes]
   [babashka.impl.classpath :as cp]
   [babashka.impl.common :as common]
   [babashka.impl.test :as t]
   [sci.addons :as addons]
   [sci.core :as sci])
  (:gen-class))

(def cp-state (atom nil))

(defn add-classpath* [add-to-cp]
  (swap! cp-state
         (fn [{:keys [:cp]}]
           (let [new-cp
                 (if-not cp add-to-cp
                         (str cp (System/getProperty "path.separator") add-to-cp))]
             {:loader (cp/loader new-cp)
              :cp new-cp})))
  nil)

(def namespaces
  {'clojure.test t/clojure-test-namespace
   'babashka.classpath {'add-classpath add-classpath*}})

;; copied from babashka
(def imports
  '{ArithmeticException java.lang.ArithmeticException
    AssertionError java.lang.AssertionError
    BigDecimal java.math.BigDecimal
    BigInteger java.math.BigInteger
    Boolean java.lang.Boolean
    Byte java.lang.Byte
    Character java.lang.Character
    Class java.lang.Class
    ClassNotFoundException java.lang.ClassNotFoundException
    Comparable java.lang.Comparable
    Double java.lang.Double
    Exception java.lang.Exception
    IllegalArgumentException java.lang.IllegalArgumentException
    Integer java.lang.Integer
    File java.io.File
    Float java.lang.Float
    Long java.lang.Long
    Math java.lang.Math
    Number java.lang.Number
    NumberFormatException java.lang.NumberFormatException
    Object java.lang.Object
    Runtime java.lang.Runtime
    RuntimeException java.lang.RuntimeException
    Process        java.lang.Process
    ProcessBuilder java.lang.ProcessBuilder
    Short java.lang.Short
    String java.lang.String
    StringBuilder java.lang.StringBuilder
    System java.lang.System
    Thread java.lang.Thread
    Throwable java.lang.Throwable})

(defn -main []
  (let [load-fn (fn [{:keys [:namespace]}]
                  (when-let [{:keys [:loader]} @cp-state]
                    (cp/source-for-namespace loader namespace nil)))
        _ (add-classpath* "./playground")
        opts {:namespaces namespaces
              :env (atom {})
              :classes classes/class-map
              :imports imports
              :load-fn load-fn}
        opts (addons/future opts)
        sci-ctx (sci/init opts)
        _ (vreset! common/ctx sci-ctx)]
    (sci/eval-string* sci-ctx (slurp "playground/faddle.clj"))))

(comment
  (t/is (= 1 1)))
