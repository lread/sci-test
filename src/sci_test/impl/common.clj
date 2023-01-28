;; portions copied from babashka.impl.common

(ns sci-test.impl.common
  (:require [sci.ctx-store :as ctx-store]))

(defn ctx [] (ctx-store/get-ctx))
