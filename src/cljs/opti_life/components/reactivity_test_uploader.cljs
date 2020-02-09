(ns opti-life.components.reactivity-test-uploader
  (:require [ajax.core :refer [POST]]))

(def file-input-id "upload-file")

(defn handle-response-ok [resp]
  (js/console.log resp))

(defn handle-response-error [ctx]
  (.log js/console "error"))

(defn send-file [evt file-name]
  (POST "/upload" {:params        {:content   (-> evt .-target .-result)
                                   :file-name file-name}
                   :handler       handle-response-ok
                   :error-handler handle-response-error}))

(defn file-read-handler [evt]
  (let [e (-> evt .-target)
        file (aget (-> e .-files) 0)
        reader (js/FileReader.)]
    (.readAsDataURL reader file)
    (.addEventListener reader "load" #(send-file % (.-name file)))))

(defn file-input-el []
  (-> js/document (.getElementById file-input-id)))

(defn upload-file-nav-item [status]
  [:li.nav-item
   [:a.nav-link {:class    status
                 :style    {:color "blue"}
                 :on-click #(-> (file-input-el) (.click))} "Upload " [:span.fa.fa-upload]]
   [:input#upload-file {:id     file-input-id
                        :name   file-input-id
                        :accept ".pdf"
                        :type   "file" :style {:display "none"}}]])
