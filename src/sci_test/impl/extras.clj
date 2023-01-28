(ns sci-test.impl.extras
  "a mishmash of namespaces need for rewrite-cljc unit tests"
  (:require
   [clojure.java.io :as io]
   [clojure.tools.reader :as tr]
   [clojure.tools.namespace.find :as ns-find]
   [clojure.tools.reader.edn :as edn]))

(def namespaces
  {'clojure.java.io {'file io/file}
   'clojure.tools.namespace.find {'find-namespaces ns-find/find-namespaces
                                  'clj ns-find/clj}
   'clojure.tools.reader {'*alias-map* tr/*alias-map*}
   'clojure.tools.reader.edn {'read-string edn/read-string}})
