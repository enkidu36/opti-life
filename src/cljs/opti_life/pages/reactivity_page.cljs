(ns opti-life.pages.reactivity-page
  (:require [opti-life.components.common :as c]
            [opti-life.components.reactivity-test-uploader :as uploader]
            [reagent.session :as session]
            [reagent.core :refer [atom]]
            [ajax.core :refer [GET]]
            [cljs-time.core :as tc]
            [cljs-time.format :as tf]))

(def details (atom []))

(def tests [[4   "2019-01-01" "pickles"]
           [2 "2019-06-01" "raspberries"]
           [3 "2020-01-01" "shrimp"]])

(defn menu []
  [:div
   [:ul.nav.justify-content-left {:style {:backgroundColor "#e6f7ff"}}
    [uploader/upload-file-nav-item "active"]
    [:li
     [:a#plan.nav-link {:class "active" } "Plan"]]]])

(def custom-formatter (tf/formatter "MM/dd/yyyy"))

(defn format-date [date]
  (tf/unparse custom-formatter (tc/date-time date)))

(defn tests-handler [resp tests]
  (js/console.log resp)
  (reset! tests (mapv #(assoc % :admin_date (format-date  (:admin_date %))) resp)))

(defn fetch-tests [user-id tests]
  (GET "/fetch-tests" {:params {:user-id user-id}
                       :handler #(tests-handler % tests)
                       :handle-error #(js/console.log %)}))

(defn get-details [evt]
  (let [id (-> evt .-target .-value)
        pred (fn [test] (= id (first test)))]
     (first (filterv pred tests))))

(defn test-item [{:keys [id admin_date]}]
  ^{:key id} [:li.list-group-item
              {:value  id
               :on-click #(reset! details (get-details %))}
              admin_date])

(defn test-list []
  (let [tests (atom [])]
    (fetch-tests "joe" tests)
    (fn []
      [:ul.list-group
       (for [test @tests]
         (test-item test))])))

(defn test-details []
  (fn []
    [:div (str (get @details 2))]))

(defn on-load-handler []
  (let [upload-el (uploader/file-input-el)]
    (.addEventListener upload-el "change" #(uploader/file-read-handler %) false)))

(defn main []
  [:div
   [:div.row
    [:div.col-2
     [test-list]]
    [:div.col-10
     [test-details]]]])

(defn page []
  (js/document.addEventListener  "DOMContentLoaded" on-load-handler)
  [:div
   [c/jumbo-tron]
   [menu]
   [c/spacer-row]
   [main]])
