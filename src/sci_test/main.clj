(ns sci-test.main
  "Effectively a very minimal version of babashka that supports interpreting tests."
  {:no-doc true}
  (:require
   [sci-test.impl.classes :as classes]
   [sci-test.impl.classpath :as cp]
   [sci-test.impl.clojure.core :as core :refer [core-extras]]
   [sci-test.impl.common :as common]
   [sci.addons :as addons]
   [sci.core :as sci]

   sci-test.impl.test
   sci-test.impl.test-check
   sci-test.impl.test-runner
   sci-test.impl.extras

   lib-under-sci-test)
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
  (merge
   {'clojure.test sci-test.impl.test/clojure-test-namespace
    'clojure.core core-extras
    'babashka.classpath {'add-classpath add-classpath*}}

   sci-test.impl.test-check/namespaces
   sci-test.impl.test-runner/namespaces
   sci-test.impl.extras/namespaces

   ;; TODO: probably better to find through some discovery
   ;; this source is generated/hand-coded by you and exposes your lib API to sci so that tests may run
   lib-under-sci-test/namespaces))

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
    IndexOutOfBoundsException java.lang.IndexOutOfBoundsException
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

(defn parse-args [args]
  (let [opts (loop [args args
                    opts-map {}]
               (if (not args)
                 opts-map
                 (let [arg (first args)]
                   (case arg
                     ("--classpath", "-cp")
                     (let [args (next args)]
                       (recur (next args)
                              (assoc opts-map :classpath (first args))))
                     ("-f" "--file")
                      (let [args (next args)]
                        (recur (next args)
                               (assoc opts-map :file (first args))))))))]
    opts))

(defn usage-help []
  (println "sci-test - An experiment in interpreting unit tests via the Small Clojure Inperpreter

  --file      -f  <filename>  * Evaluate the contents of filename, likely a test runner,
                                for example: sci_test_runner.clj

  --classpath -cp <classpath>   Sci classpath.
                                for example: ./script"))


(defn -main [& args]
  (let [{:keys [:file :classpath]} (parse-args args)
        _ (when (not file)
            (usage-help)
            (System/exit 1))
        load-fn (fn [{:keys [:namespace]}]
                  (when-let [{:keys [:loader]} @cp-state]
                    (cp/source-for-namespace loader namespace nil)))
        _ (when classpath (add-classpath* classpath))
        opts {:namespaces namespaces
              :env (atom {})
              :features #{:sci-test :clj}
              :classes classes/class-map
              :imports imports
              :load-fn load-fn}
        opts (addons/future opts)
        sci-ctx (sci/init opts)
        _ (vreset! common/ctx sci-ctx)]
    (sci/binding [sci/out *out*
                  sci/err *err*]
      (sci/eval-string* sci-ctx (slurp file)))))
