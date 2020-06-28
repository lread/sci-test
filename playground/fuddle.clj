(ns fuddle
  (:require [clojure.test :as t]
            [rewrite-cljc.zip :as z]))

(defn callme[]
  (t/is (= 1 (z/of-string "(def boo 2)"))))
