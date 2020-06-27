(ns sci-test.main
  {:no-doc true}
  (:require
   [babashka.impl.classes :as classes]
   [babashka.impl.common :as common]
   [babashka.impl.test :as t]
   [sci.addons :as addons]
   [sci.core :as sci])
  (:gen-class))

(def namespaces
  {'clojure.test t/clojure-test-namespace})

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
  (let [opts {:namespaces namespaces
              :env (atom {})
              :classes classes/class-map
              :imports imports}
        opts (addons/future opts)
        sci-ctx (sci/init opts)
        _ (vreset! common/ctx sci-ctx)]
    (sci/eval-string* sci-ctx "(require '[clojure.test :as t]) (t/is (= 1 2))")))

(comment
  (t/is (= 1 1)))
