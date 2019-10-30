(ns opti-life.pages.scroll-page
  (:require
    [reagent.session :as session]
    [reagent.core :as reagent :refer [atom]]
    [opti-life.components.common :as c]
    [opti-life.components.scroll :as scroll]
    [opti-life.components.plan.calendar-header :as cal-header]))

(def page-size 10)
(def db
  (atom {:items (map inc (range page-size))}))

(defn items-list [items]
  [:ul {:style {:list-style-type :none}}
   (for [item items]
     ^{:key item}
     [:li {:style {:height "100px" :width "400px"
                   :border "1px solid gray"}}
      [:div "Item number " item]])])

(defn main []
  (fn []
    (let [items (:items @db)
          loading? (:loading? @db)
          more-items-available? (< (count items) 60)]
      [:div.pl-1
       [cal-header/cal-header-table]
       [:div#plan-cal.scroll
         [items-list items]
         [:div
          (cond
            loading?
            "Loading more..."

            more-items-available?
            "Scroll to here"

            :else
            "No more items!!")]
         [scroll/infinite-scroll {:can-show-more? more-items-available?
                                  :container-id "plan-cal"
                                  :load-fn (fn []
                                             (swap! db assoc :loading? true)
                                             (js/setTimeout
                                               (fn []
                                                 (swap! db assoc :loading? false)
                                                 (swap! db update :items
                                                        (fn [items]
                                                          (concat items (range (inc (last items))
                                                                               (+ (last items) (inc page-size)))))))
                                               1000))}]]])))

(defn page []
  [:div
   [c/menu-w-drpdown]
   [c/spacer-row]
   [:div#scroll-height.ml-4 0]
   [:div#scroll-position.ml-4 0]
   [:div#scroll-top.ml-4 0]
   [:div.row
    [:div.col-md-12
     (if (nil? (session/get :identity))
       [c/greeting]
       [main])]]])