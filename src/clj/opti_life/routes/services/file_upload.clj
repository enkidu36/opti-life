(ns opti-life.routes.services.file-upload
  (:require [cheshire.core :refer [generate-string]]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [buddy.core.codecs.base64 :as b64]
            [ring.util.http-response :as response]
            [clojure.tools.logging :as log]
            [opti-life.db.core :as db])
  (:import (java.io ByteArrayInputStream)
           (org.apache.pdfbox.text PDFTextStripper)
           (org.apache.pdfbox.pdmodel PDDocument)
           (java.text SimpleDateFormat)))

(defn fetch-user-tests [{:keys [params]}]
  (try
     (let [data (db/get-user-test-metadata-by-user {:user_id (:user-id params)})]
       (log/info data)
       (response/ok data))
     (catch Exception e
       (log/error (.getNextException e)))))

(defn make-response [status value-map]
  {:status  status
   :headers {"Content-Type" "application/json"}
   :body    (generate-string value-map)})

(defn pdf? [type]
  (if type
   (str/includes? type "application/pdf")
   false))

(defn split-content [content]
  ;; returns a vector of document type and document
  (let [[x doc] (str/split content #",")
        doc-type (re-find (re-matcher #"application/pdf" x))]
    (conj [] doc-type doc)))

(defn decode [content]
  (let [doc (second content)]
    (replace {doc (b64/decode doc)} content)))

(defn save-file [user-id admin-date content]
  (try
    (let [saveMap {:user_id user-id, :admin_date admin-date, :file_content content, :file_type "application/pdf"}]
      (first (db/create-user-test! saveMap)))
    (catch Exception e
      (log/error (.printStackTrace e)))))

(def admin-date-pattern #"REF! (\d{1,2}\/\d{1,2}\/\d{4}) (\d{1,2}\/\d{1,2}\/\d{4})")
(defn read-pdf-text [doc]
  (try
    (let [pdf-doc (PDDocument/load doc)
          stripper (PDFTextStripper. )
          text (str/replace (.getText stripper pdf-doc) #"\r\n" " ")]
       (.close pdf-doc)
       text)
    (catch Exception e (prn e))) )

(defn format-date [date format]
  (.parse (SimpleDateFormat. format) date) )

(defn read-admin-date [doc]

  (let [docByteStream (ByteArrayInputStream. doc)
        pdf-text (read-pdf-text docByteStream)]
    (log/info "read-admin-date")
    (log/info pdf-text)
    (-> admin-date-pattern
        (re-matcher pdf-text)
        (re-find)
        (nth 2)
        (format-date "MM/dd/yyyy"))))


(defn save-pdf [doc]
  (let [docByteStream (ByteArrayInputStream. doc)
        admin-date (read-admin-date doc)]
    (log/info "save-pdf")
    (save-file "joe" admin-date doc)))

(defn handle-upload [{:keys [params]}]
  (log/info "handle-upload")
  (let [[doc-type doc] (-> (:content params) (split-content) (decode))
        file-name (:file-name params)]
    (if (and (pdf? doc-type) (> (count doc) 0))
      (response/ok (save-pdf doc))
      (make-response 400 {:status   "Bad Request"
                          :filename file-name
                          :message "Document must be a .pdf and not empty"}))))


