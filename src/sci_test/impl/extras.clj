(ns sci-test.impl.extras
  "a mishmash of namespaces need for rewrite-cljc unit tests"
  (:require
   [clojure.java.io :as io]
   [clojure.tools.reader :as tr]
   [clojure.tools.namespace.find :as ns-find]
   [clojure.tools.reader.edn :as edn]
   [sci.core :as sci]))

(def reader-ns (sci/create-ns 'clojure.tools.reader nil))
(def read-eval (sci/new-dynamic-var '*read-eval* true {:ns reader-ns}))

(def namespaces
  {'clojure.java.io {'file io/file}
   'clojure.tools.namespace.find {'find-namespaces ns-find/find-namespaces
                                  'clj ns-find/clj}
   'clojure.tools.reader {'*alias-map* tr/*alias-map*
                          '*read-eval* read-eval
                          'read-string tr/read-string}
   'clojure.tools.reader.edn {'read-string edn/read-string}})
