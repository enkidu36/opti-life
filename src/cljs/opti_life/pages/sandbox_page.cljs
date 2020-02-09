(ns opti-life.pages.sandbox-page
  (:require [ajax.core :refer [POST]]
            [reagent.core :refer [atom]]
            [reagent.session :as session]
            [goog.events :as events])
  (:import goog.net.IframeIo
           goog.net.EventType))

(defn status-component []
  (if-let [status (session/get :upload-status)]
    [:div
     [:h3 "Status"]
     status]))

(defn into-list [items]
  (into [:ul]
        (for [i items]
          [:li i])))

(defn set-upload-indicator []
  (let [class "fa fa-spinner fa-spin fa-pulse"]
    (session/put! :upload-status [:div
                                  [:p "Uploading file... "
                                   [:span {:class class}]]])))

(defn set-status [class title items]
  [:div {:class class}
   [:h4 title]
   (into-list items)])


(defn upload-component []
  [:div
   [:form {:id       "upload-form"
           :enc-type "multipart/form-data"
           :method   "POST"}
    [:label {:style {:padding-right 10}} "Upload Filename:"]
    [:input {:type "file"
             :name "upload-file"
             :id   "upload-file"}]]])

;;; goog.net.IFrameIO routines
(defn iframe-response-ok [msg]
  (let [status (set-status "alert alert-success"
                           "Upload Successful"
                           [(str "Filename: " (:filename msg))])]
    (session/put! :upload-status status)))

(defn iframe-response-error [msg]
  (let [status (set-status "alert alert-danger"
                           "Upload Failure"
                           [(str "Status: " (:status msg))
                            (str (:message msg))])]
    (session/put! :upload-status status)))

(defn handle-iframe-response [json-msg]
  (let [msg (js->clj json-msg :keywordize-keys true)]
    (.log js/console (str "iframe-response: " msg))
    (cond
      (= "OK" (:status msg)) (iframe-response-ok msg)
      (= "ERROR" (:status msg)) (iframe-response-error msg)
      :else (session/put! :upload-status [:div.alert.alert-danger
                                          [:h4 "Unexpected Error"]
                                          [:ul
                                           [:li (str "Status: " (:status msg))]
                                           [:li (:message msg)]]]))))
(defn iframeio-upload-file [form-id]
  (let [el (.getElementById js/document form-id)
        blob (js/Blob. (aget (.-files (aget el "upload-file")) 0))
        iframe (IframeIo.)]
    ;(events/listen iframe EventType.COMPLETE
    ;               (fn [event]
    ;                 (let [rsp (.getResponseJson iframe)
    ;                       status ()])
    ;                 (handle-iframe-response (.getResponseJson iframe))
    ;                 (.dispose iframe)))
    ;(set-upload-indicator)
    ;(.sendFromForm iframe el "/upload")
    ))

(defn iframeio-upload-button []
  [:div
   [:hr]
   [:button {:class    "btn btn-primary"
             :type     "button"
             :on-click #(iframeio-upload-file "upload-form")}
    "Upload Test " [:span {:class "fa fa-upload"}]]])

(defn show-test []
  (if-let [modal (session/get :reactivity-upload)]
    [modal]))

(defn show-upload-status []
  (if-let [modal (session/get :upload-status)]
    [modal]))

(defn page []
  [:div
   [ort/upload-button]
   [show-test]
   [show-upload-status]
   ;[status-component]
   ;[upload-component]
   ;[iframeio-upload-button]
   ])
