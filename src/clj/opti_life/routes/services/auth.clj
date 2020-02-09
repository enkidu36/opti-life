(ns opti-life.routes.services.auth
  (:require
    [opti-life.db.core :as db]
    [opti-life.validation :refer [registration-errors]]
    [ring.util.http-response :as response]
    [buddy.hashers :as hashers]
    [clojure.tools.logging :as log]))

(defn handle-registration-error [e]
  (if (and (instance? java.sql.SQLException e)
           (-> e
               (.getNextException)
               (.getMessage)
               (.startsWith "ERROR: duplicate key value")))
    (response/precondition-failed {:result :error :message "user with the selected ID already exists"})
    (do
      (log/error e)
      (response/internal-server-error {:result :error :message "server error occurred while adding the user"}))))

(defn register! [{:keys [session]} user]
  (if (registration-errors user)
    (response/precondition-failed {:result :error})
    (try
      (db/create-user!
        (-> user
            (dissoc :pass-confirm)
            (update :pass hashers/encrypt)))
      (-> {:result :ok}
          (response/ok)
          (assoc :session (assoc session :identity (:id user))))
      (catch Exception e
        (log/error e)
        (handle-registration-error e)))))

(defn decode-auth [encoded]
  (log/info "decode-auth ")
  (let [auth (second (.split encoded " "))]
    (log/info (str "Auth String: " (String. (.decode (java.util.Base64/getDecoder) auth) (java.nio.charset.Charset/forName "UTF-8"))))
    (-> (.decode (java.util.Base64/getDecoder) auth)
        (String. (java.nio.charset.Charset/forName "UTF-8"))
        (.split ":"))))

(defn authenticate [[id pass]]
  (log/info (str "authenticate id/pass: " id "/" pass))
  (when-let [user (db/get-user {:id id})]
    (log/info (str "dbPass: " (:pass user)))
    (when (hashers/check pass (:pass user))
      id)))

(defn login! [{:keys [session]} auth]
  (log/info auth)
  (if-let [id (authenticate (decode-auth auth))]
    (-> {:result :ok}
        (response/ok)
        (assoc :session (assoc session :identity id)))
    (response/unauthorized {:result :unauthorized :message "login failure"})))

(defn logout! []
  (-> {:result :ok}
      (response/ok)
      (assoc :session nil)))