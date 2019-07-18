(ns opti-life.routes.services.food
  (:require [opti-life.db.core :as db]
            [ring.util.http-response :as response]
            [clojure.tools.logging :as log]))

(defn get-all-categories []
  (try
    (response/ok (db/all-food-categories))
     (catch Exception e
       (log/error e)
       (response/internal-server-error {:result :error :message "server error occurred while adding the user"})   )))
