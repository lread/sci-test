(ns sci-test.impl.test-check
  "A small sampling of test.check - currently all I need for rewrite-cljc"
  (:require [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :as clj-check]
            [clojure.test.check :as check]
            [sci.core :as sci]))

(def gen-ns (sci/create-ns 'clojure.test.check.generators))
(def prop-ns (sci/create-ns 'clojure.test.check.properties))
(def clj-check-ns (sci/create-ns 'clojure.test.check.clojure-test))
(def check-ns (sci/create-ns 'clojure.test.check))

(def namespaces
  {'clojure.test.check.generators
   {'vector (sci/copy-var gen/vector gen-ns)
    'elements (sci/copy-var gen/elements gen-ns)
    'fmap (sci/copy-var gen/fmap gen-ns)
    'tuple (sci/copy-var gen/tuple gen-ns)
    'string-ascii (sci/copy-var gen/string-ascii gen-ns)
    'such-that (sci/copy-var gen/such-that gen-ns)
    'sample (sci/copy-var gen/sample gen-ns)
    'int (sci/copy-var gen/int gen-ns)
    'symbol (sci/copy-var gen/symbol gen-ns)
    'return (sci/copy-var gen/return gen-ns)
    'choose (sci/copy-var gen/choose gen-ns)
    'keyword (sci/copy-var gen/keyword gen-ns)
    'boolean (sci/copy-var gen/boolean gen-ns)
    'generator? (sci/copy-var gen/generator? gen-ns)
    'bind (sci/copy-var gen/bind gen-ns)}
   'clojure.test.check.clojure-test
   {'defspec (sci/copy-var clj-check/defspec clj-check-ns)
    'assert-check (sci/copy-var clj-check/assert-check clj-check-ns)
    'process-options (sci/copy-var clj-check/process-options clj-check-ns)}
   'clojure.test.check.properties
   {'for-all (sci/copy-var prop/for-all prop-ns)
    'for-all* (sci/copy-var prop/for-all* prop-ns)}
   'clojure.test.check
   {'quick-check (sci/copy-var check/quick-check check-ns)}})
