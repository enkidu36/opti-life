(ns opti-life.components.plan.calendar-header)

(def days ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"])
(defn cal-header-table []
  [:table.table.cal-table.mb-0
   [:tbody
    [:tr
     (for [day days]
       [:th.cal-header-day-col {:key day } day])
     [:th.cal-summary-header-col ""]]]])

