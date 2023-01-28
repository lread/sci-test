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
   [sci.ctx-store :as ctx-store]
   sci-test.impl.test
   sci-test.impl.test-check
   sci-test.impl.test-runner
   sci-test.impl.extras

   lib-under-sci-test.defs)
  (:gen-class))

(def namespaces
  (merge
   {'clojure.test sci-test.impl.test/clojure-test-namespace
    'clojure.core core-extras
    'babashka.classpath {'add-classpath cp/classpath-namespace}}

   sci-test.impl.test-check/namespaces
   sci-test.impl.test-runner/namespaces
   sci-test.impl.extras/namespaces

   ;; TODO: probably better to find through some discovery
   ;; this source is generated/hand-coded by you and exposes your lib API to sci so that tests may run
   lib-under-sci-test.defs/namespaces))

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
                  (when-let [{:keys [:loader]} @cp/cp-state]
                    (cp/source-for-namespace loader namespace nil)))
        _ (when classpath (cp/add-classpath classpath))
        opts {:namespaces namespaces
              :env (atom {})
              :features #{:sci-test :clj}
              :classes classes/class-map
              :imports classes/imports
              :load-fn load-fn}
        opts (addons/future opts)
        sci-ctx (sci/init opts)
        _ (ctx-store/reset-ctx! sci-ctx)]
    (sci/binding [sci/out *out*
                  sci/err *err*]
      (sci/eval-string* sci-ctx (slurp file)))))
