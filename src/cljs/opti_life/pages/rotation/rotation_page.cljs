(ns opti-life.pages.rotation.rotation-page
  (:require [reagent.session :as session]
            [opti-life.pages.rotation.calendar :refer [calendar]]
            [opti-life.components.common :as c]))

(defn page []
  [:div.test-class
   (if (nil? (session/get :identity))
     [c/greeting]
     [calendar])])
