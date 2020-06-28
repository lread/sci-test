(ns fuddle
  (:require [clojure.test :as t]))

(defn callme[]
  (t/is (= 1 2)))
