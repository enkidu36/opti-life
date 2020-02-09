(ns opti-life.routes.services.user-test
  (:require [opti-life.db.core :as db]
            [ring.util.http-response :as response]
            [clojure.tools.logging :as log]))

(defn add-user-test! [user-test]
  (try
    (log/info user-test)
    (db/create-user-test! user-test)
    (-> {:result :ok}
        (response/ok))
    (catch Exception e
      (log/error (.getNextException e)))))
