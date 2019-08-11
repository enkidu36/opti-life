(ns opti-life.components.foods
  (:require [reagent.core :as r]
            [reagent.session :as session]
            [clojure.string :as string]
            [ajax.core :refer [POST GET]]
            [opti-life.components.common :as c]))

(def categories ["Fruits", "Vegetables", "Root Vegetables",
                 "Beans & Peas", "Grains", "Meats & Poultry",
                 "Seafood", "Dairy", "Seeds & Nuts", "Herbs & Spices",
                 "Beverages", "Major Offenders"])

(def reactivity ["Safe", "Mild", "Moderate", "Severe"])

(defn add-food! [fields errors]
  (POST "/add-food" {:params @fields
                     :handler #(do (reset! fields {})
                                   (-> js/document (.querySelector "input[type='text']") (.focus)))
                     :error-handler #(reset! errors {:server-error (get-in % [:response :message])})}))

(defn add-form []
  (let [fields (r/atom {})
        errors (r/atom nil)]
    (fn []
      [:div {:on-key-press (fn [e] (when (and (not-empty @fields) (= 13 (.-charCode e)))
                                     (add-food! fields errors)))}
       [:div.form-row
        [:div.col
         [c/text-input "Food" :name "Food name" fields false]]
        [:div.col [c/select-input "Category" :food_category "Fruits  Veg Root Beans Grains Meat Dairy Nut Herb Bev Maj " fields categories ]]]
       ;[:div.form-row
       ; [:div.col [c/text-number "Calories" :calories "Calories per gram" fields true]]
       ; [:div.col [c/text-number "Carbs" :carbs "Carbs per gram" fields true]]
       ; [:div.col [c/text-number "Fats" :fats "Fats per gram" fields true]]]
       ;[:div.form-row
       ; [:div.col [c/text-number "Protein" :protein "Protein per gram" fields true]]
       ; [:div.col [c/text-number "Sugars" :sugars "Sugars per gram" fields true]]
       ; [:div.col [c/text-number   "Sodium" :sodium "Sodium per gram" fields true]]]
       [:div.form-row
        [:div
         [:button.btn.btn-primary
          {:on-click #(add-food! fields errors)} "Add"]]]])))

(defn fetch-handler [db data]
  (reset! db (group-by :food_category data)))

(defn fetch-foods [db]
      (GET "/all-foods" {:handler #(fetch-handler db %)}))

(defn food-group [category foods]
  [:card
   [:card-body
    [:h5 {:style {:text-decoration "underline"}} category]
    (for [item (get @foods category)]
      [:p (:name item)])]])

(defn food-table []
  (let [db (r/atom {})]
    (fetch-foods db)
    (fn []
      [:div.row {:style {:margin-left 120}}
       [:div.col
        (for [index (range 3) ]
          [food-group (nth categories index) db])]
       [:div.col
        (for [index (range 3 8)]
          [food-group (nth categories index) db])]
       [:div.col
        (for [index (range 8 12)]
          [food-group (nth categories index) db])]])))



