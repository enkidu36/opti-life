(ns opti-life.pages.scroll-page
  (:require
    [reagent.session :as session]
    [reagent.core :as reagent :refer [atom]]
    [opti-life.components.common :as c]
    [opti-life.components.scroll :as scroll]
    [opti-life.components.plan.calendar-header :as cal-header]
    [opti-life.components.date-util :as date-utils]))

(def page-size 3 )

(defn make-week [list]
  (take page-size (partition 7 date-utils/db)))

(def db
  (atom {:init true
         :items (make-week date-utils/db)}))

(defn items-list [weeks]
  [:div
   (for [week weeks]
     [:div.calendar {:style {:backgroundColor "red"}}
       (for [day week]
         [:div.week-day.day-header (str " " (date-utils/day-with-new-month (:date day)))])
        [:div.day-header.summary.summary-week "Summary"]
      (for [day week]
           [:div.week-day.day-body
            [:div.activity-title "Today's menu"]
           (for [meal (:meals day)]
            [:div.key-stats (str (:name meal) "     "  (-> meal :foods (first) :name))])])
        [:div.week-day.summary.summary-body "body"]])])

(defn main []
  (fn []
    (let [items (:items @db)
          loading? (:loading? @db)
          more-items-available? (< (count items) 15)]
      [:div {:style {:backgroundColor "yellow"
                     :height "100%"}}
       [cal-header/cal-week-header]
       [:div#plan-cal.scroll
          [items-list items]
         [:div
          (cond
            loading?
            "Loading more..."

            more-items-available?
            "Scroll to here"

            :else
            "No more items!!!")]
         [scroll/infinite-scroll {:can-show-more? more-items-available?
                                  :container-id "plan-cal"
                                  :back-fn (fn []
                                             (swap! db assoc :loading? true)
                                             (js/setTimeout
                                               (fn []
                                                 (swap! db assoc :loading? false)
                                                 (swap! db update :items
                                                        (fn [items]
                                                          (concat (partition 7 date-utils/db-back) items))))
                                               1000))
                                  :forward-fn (fn []
                                                (swap! db assoc :loading? true)
                                                (js/setTimeout
                                                  (fn []
                                                    (swap! db assoc :loading? false)
                                                    (if (:init @db)
                                                      (do
                                                        (swap! db assoc :init false)
                                                        (swap! db update :items
                                                           (fn [items]
                                                                   (concat items (make-week date-utils/db)))))
                                                        (swap! db update :items
                                                               (fn [items]
                                                                 (concat items (partition 7 date-utils/db-forward))))))
                                                  1000))}]]])))

(defn page []
  [:div {:style {:backgroundColor "orange"
                 :height "100%"}}
   ;[c/menu-w-drpdown]
   ;[c/spacer-row]
   (let [count (count (:items @db))]
     [:div [:p (str count)]]
     )

   [:div#scroll-height.ml-4 0]
   [:div#scroll-position.ml-4 0]
   [:div#scroll-top.ml-4 0]
     (if (nil? (session/get :identity))
       [c/greeting]
       [main])])