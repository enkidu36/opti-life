(ns opti-life.components.login
  (:require [reagent.core :as r]
            [reagent.session :as session]
            [goog.crypt.base64 :as b64]
            [clojure.string :as string]
            [ajax.core :refer [POST]]
            [opti-life.components.common :as c]))

(def timeout-ms (* 1000 60 30))

(defn session-timer []
  (when (session/get :identity)
    (if (session/get :user-event)
      (do
        (session/remove! :user-event)
        (js/setTimeout #(session-timer) timeout-ms))
      (session/remove! :identity))))

(defn encode-auth [user pass]
  (->> (str user ":" pass) (b64/encodeString) (str "Basic ")))

(defn login! [fields error]
  (let [{:keys [id pass]} @fields]
    (reset! error nil)
    (POST "/login"
               {:headers {"Authorization" (encode-auth (string/trim id) pass)}
                :handler #(do
                            (session/remove! :modal)
                            (session/put! :identity id)
                            (js/setTimeout session-timer timeout-ms)
                            (reset! fields nil))
                :error-handler #(reset! error (get-in % [:response :message]))})))

(defn login-form []
  (let [fields (r/atom {})
        error (r/atom nil)]
     (fn []
      [c/modal
       [:div "Opti-Life Login"]
       [:div#username {:on-key-press (fn [e] (when (and (not-empty @fields) (= 13 (.-charCode e)))
                                             (login! fields error)))}
        [:div.well.well-sm [:strong "* required field"]]
        [c/text-input-focus "name" :id "enter a user name" fields ]
        [c/password-input "password" :pass "enter a password" fields ]
        (when-let [error @error]
          [:div.alert.alert-danger error])]
       [:div
        [:button.btn.btn-primary
         {:on-click #(login! fields error) :value ""} "Login"]
        [:button.btn.btn-danger {:on-click #(session/remove! :modal)} "Cancel"]]])))

(defn login-button []
  [:a.btn {:on-click #(session/put! :modal login-form)} "login"])
