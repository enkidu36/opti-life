(ns opti-life.validation
  (:require [bouncer.core :as b]
            [bouncer.validators :as v]))

(defn registration-errors [{:keys [pass-confirm] :as params}]
  (first
    (b/validate
      params
      :id v/required
      :pass [v/required
             [v/min-count 7 :message "pass-word must contain at leas 8 characters"]
             [= pass-confirm :message "re-entered password does not match"]])))
