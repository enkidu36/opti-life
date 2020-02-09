(ns opti-life.pages.rotation.calendar
  (:require [opti-life.components.date-util :as du]
            [reagent.core :as reagent]))

(def db (reagent/atom {:weeks      {:number 1}
                       :activities [{:id   1 :date (du/get-today) :body "I'm a teapot!"}
                                    {:id   2 :date (du/get-today) :body "I'm a teapot!!"}
                                    {:id   3 :date (du/plus-day (du/get-today) 1) :body "I'm a teapot!!"}
                                    {:id   4 :date (du/plus-day (du/get-today) 2) :body "I'm a teapot!!"}]}))

(defn day-header []
  [:div.day-header
   (for [day du/days]
     ^{:key day} [:div.day.day-width day])])

(defn cal-add-activity [date]
  [:div.add-activity {:on-click
                      #(swap! db update-in [:activities] conj {:id  (+ 1 (reduce max (mapv (fn [x] (:id x)) (:activities @db)))) :date date :body "test"}) }
   "+"])

(defn cal-cell-header [date]
  (let [format (if (du/first-day-of-month? date) "MMM d" "d")]
    [:div.cal-cell-header
     (du/date->string date format)]))

(defn cal-cell-activities [date]
  (let [activities (filter #(du/equal-date? date (:date %)) (:activities @db))]
    [:div
     (for [activity activities]
       ^{:key (:id activity)}[:div (:body activity)])]))

(defn cal-cell-body [date]
  [:div
   [cal-cell-activities date]
   [cal-add-activity date]])

(defn cal-cell [first-day]
  (for [date (du/build-week first-day)]
    ^{:key date} [:div.day.day-width
                  [cal-cell-header date]
                  [cal-cell-body date]]))

(defn cal-row [first-day]
  [:div.cal-row (cal-cell first-day)])

(defn get-weeks [count]
  (-> (du/get-today)
      (du/last-monday)
      (du/build-weeks count)))

(defn calendar []
  [:div.calendar-container
   [day-header]
   (for [first-day (get-weeks (get-in @db [:weeks :number]))]
     ^{:key first-day} [cal-row first-day])])

