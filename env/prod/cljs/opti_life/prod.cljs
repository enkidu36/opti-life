(ns opti-life.app
  (:require [opti-life.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
