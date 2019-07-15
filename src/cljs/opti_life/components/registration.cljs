(ns opti-life.components.registration
  (:require [reagent.core :refer [atom]]
            [opti-life.components.common :as c]
            [ajax.core :as ajax]
            [reagent.session :as session]
            [opti-life.validation :refer [registration-errors]]))

(defn register! [fields errors]
  (reset! errors (registration-errors @fields))
  (when-not @errors
    (ajax/POST "/register"
               {:params @fields
                :handler #(do
                            (session/put! :identity (:id @fields))
                            (reset! fields {})
                            (session/remove! :modal))
                :error-handler
                #(reset! errors {:server-error (get-in % [:response :message])})})))

(defn registration-form []
  (let [fields (atom {})
        error (atom nil)]
    (fn []
      [c/modal
       [:div "Opti-Life Registration"]
       [:div {:on-key-press (fn [e] (when (and (not-empty @fields) (= 13 (.-charCode e)))
                                      (register! fields error)))}
        [:div.well.well.sm [:strong "* required field"]]

        [c/text-input "Name" :id "enter a user name" fields]
        (when-let [error (first (:id @error))]
          [:div.alert.alert-danger error])
        [c/password-input "Password" :pass "enter a password" fields]
        (when-let [error(first (:pass @error))]
          [:div.alert.alert-danger error])
        [c/password-input "Password" :pass-confirm "re-enter a password" fields]

        (when-let [error (:server-error @error)]
          [:div.alert.alert-danger error])
        ]

       [:div
        [:button.btn.btn-primary {:on-click #(register! fields error)} "Register"]
        [:button.btn.btn-danger {:on-click #(session/remove! :modal)} "Cancel"]]])))

(defn registration-button []
  [:a.btn
   {:on-click #(session/put! :modal registration-form)}
   "register"])

