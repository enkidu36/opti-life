(ns opti-life.pages.plan-page
  (:require [opti-life.components.common :as c]
            [opti-life.components.date-util :as d]
            [reagent.session :as session]
            [reagent.core :as reagent]))

(defn cal-header-table []
  [:table.table.cal-table.mb-0
   [:tbody
    [:tr
     (for [day d/days]
       [:th.cal-header-day-col {:key day } day])
     [:th.cal-summary-header-col ""]]]])

(defn cal-body-table-x [dates]
  (reagent/create-class
    {
     :display-name "cal-body-table"
     :component-did-mount (fn [this]
                            (prn (-> js/document
                                     (.querySelector "#plan-cal")
                                     (.-scrollTop)
                                     (set! 460))))
     :reagent-render
     (fn [dates]
       [:table.table.cal-table.mt-0
        [:tbody
         (for [row (range 6)]
           ;; Column body row
           [:tr {:key row}
            ;; Calendar day columns
            (for [col (range (count d/days))]
              (let [date (nth dates (+ (* row 7) (+ col 0)))
                    month (d/date->string date "MMMM")
                    day (d/date->string date "d")]
                [:td.cal-day.cal-body.pt-0.pb-0 {:key date}
                 [:div {:style {:backgroundColor "yellow" :text-align "left"}} (if (or (= day "1") (and (= row 0) (= col 0)))
                                                                                 (str month " " day)
                                                                                 day)]
                 [:div.p-1
                  [:card
                   [:div {:style {:backgroundColor "#e6f7ff"}}
                    [:h4 (str "Group: " (mod (+ (* row 7) (+ col 1)) 5))]
                    [:p (str "Breakfast: ")]
                    [:p "Lunch"]
                    [:p "Dinner"]
                    [:p "Snack"]]]]]))

            ;; Summary Column
            [:td.cal-summary-col
             [:div {:style {:backgroundColor "#e7f7ff" :text-align "left"}} "Summary"]
             [:card
              [:div.container {:style {:border-style "solid"}}
               [:h4 "Christine Zorad"]
               [:p "This is your life.... a very long paragraph"]]]]])]])}))

(defn cal-body-table [dates]
  [:table.table.cal-table.mt-0
   [:tbody
    (for [row (range 6)]
      ;; Column body row
      [:tr {:key row}
       ;; Calendar day columns
       (for [col (range (count d/days))]
         (let [date (nth dates (+ (* row 7) (+ col 0)))
               month (d/date->string date "MMMM")
               day (d/date->string date "d")]
           [:td.cal-day.cal-body.pt-0.pb-0 {:key date}
            [:div {:style {:backgroundColor "yellow" :text-align "left"}} (if (or (= day "1") (and (= row 0) (= col 0)))
                                                                            (str month " " day)
                                                                            day)]
            [:div.p-1
             [:card
              [:div {:style {:backgroundColor "#e6f7ff"}}
               [:h4 (str "Group: " (mod (+ (* row 7) (+ col 1)) 5))]
               [:p (str "Breakfast: ")]
               [:p "Lunch"]
               [:p "Dinner"]
               [:p "Snack"]]]]]))

       ;; Summary Column
       [:td.cal-summary-col
        [:div {:style {:backgroundColor "#e7f7ff" :text-align "left"}} "Summary"]
        [:card
         [:div.container {:style {:border-style "solid"}}
          [:h4 "Christine Zorad"]
          [:p "This is your life.... a very long paragraph"]]]]])]])

(defn calendar [dates]
  [:div.pl-1
    [cal-header-table]
    [:div#plan-cal.scroll
     [cal-body-table-x dates]]])

(defn main [dates]
  [calendar dates])

(defn page []
  (let [dates (d/build-calendar-dates)]
      [:div
     [c/menu-w-drpdown]
     [c/spacer-row]
     (if (nil? (session/get :identity))
       [c/greeting]
       [main dates])]))


