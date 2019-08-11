(ns opti-life.pages.food-page
  (:require [reagent.session :as session]
            [ajax.core :refer [GET]]
            [opti-life.components.common :as c]
            [opti-life.components.foods :as f]))


(defn main []
  [:div
   ;[f/add-form]
   [f/food-table]])

(defn page []
  [:div.container
   [c/jumbo-tron]
   [:div.row
    [:div.col-md-12
     (if (nil? (session/get :identity))
       [c/greeting]
       [main])]]])



