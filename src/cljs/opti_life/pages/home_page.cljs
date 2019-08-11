(ns opti-life.pages.home-page
  (:require [reagent.session :as session]
            [opti-life.components.common :as c]
            [markdown.core :refer [md->html]]))


(defn main []
  [:h2 "Welcome Opti-Life - Let's get started :)"]  )

(defn page []
  [:div.container
   [c/jumbo-tron]
   [:div.row
    [:div.col-md-12
     (if (nil? (session/get :identity))
       [c/greeting]
       [main])]]])



