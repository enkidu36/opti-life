(ns opti-life.components.plan.calendar-header)

(def days ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"])

(defn cal-week-header []
  [:div.calendar
   (for [day days]
     [:div.week-header {:key day} day])
   [:div.week-header.summary-header "n" ]] )

