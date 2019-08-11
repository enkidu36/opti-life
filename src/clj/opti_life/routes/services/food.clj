(ns opti-life.routes.services.food
  (:require [opti-life.db.core :as db]
            [ring.util.http-response :as response]
            [clojure.tools.logging :as log]))

(defn add-food [food]
  (try
    ;; merge in default values for HugSql.
    ;; Is there a better way?
    (let [saveMap (merge {:calories nil, :protein nil, :fats nil, :carbs nil, :sugars nil, :sodium nil} food)]
      (db/create-food saveMap)

      (-> {:result :ok}
          (response/ok)))
    (catch Exception e
       (log/error (.getNextException e)))))

(defn all-foods []
  (try
    (let [data (db/all-foods)]
      (log/info data)
        (response/ok data))
   (catch Exception e
      (log/error (.getNextException e)))))
