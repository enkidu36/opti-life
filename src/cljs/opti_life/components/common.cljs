(ns opti-life.components.common
  (:require [reagent.session :as session]))

(defn jumbo-tron []
[:div.jumbotron
 [:h1 "Dr Zorad's Opti-Life"]
 (when (session/get :identity)
   [:div
    [:a {:href "#/food"} "food"] " | "
    [:a {:href "#/plan"} "plan"]])])

(defn menu []
  [:div
   (when (session/get :identity)
     [:div.row.justify-content-center {:style {:backgroundColor "#e6f7ff"}}
      [:div.col-4.pt-2.pb-2 {:style {:text-align "center"}}
       [:a {:href "#/food"} "food"] " | "
       [:a {:href "#/plan"} "plan"]]])])

(defn menu-w-drpdown []
  [:div
   (when (session/get :identity)
     [:ul.nav.justify-content-center {:style {:backgroundColor "#e6f7ff"}}
      [:li.nav-item
       [:a.nav-link {:href "#/food"} "Food"]]
      [:li.nav-item.dropdown
       [:a.nav-link.dropdown-toggle {:data-toggle "dropdown"
                                     :href "#" :role "button"
                                     :aria-haspopup "true"
                                     :aria-expanded "false"} "Dropdown"]
       [:div.dropdown-menu
        [:a.dropdown-item {:href "#"} "Action"]
        [:a.dropdown-item {:href "#"} "Another Action"]
        [:a.dropdown-item {:href "#"} "Never gets old"]]]
      [:li
       [:a.nav-link {:class "active" :href "#/plan"} "Plan"]]
      [:li
       [:a.nav-link.disabled {:href"#" :tabindex -1 :aria-disabled "true"} "Disabled"]] ])])

(defn menu3 []
  (when (session/get :identity)
    [:nav.nav.nav-pills.nav-justified {:style {:backgroundColor "#e6f7ff"}}
     [:a.nav-item.nav-link {:href "#/food"} "This is very long and should be of equal length and maybe wrapp"]
     [:a.nav-item.nav-link {:class "active" :href "#/plan"} "Plan"]
     [:a.nav-item.nav-link.disabled {:href"#" :tabindex -1 :aria-disabled "true"} "Disabled"] ])  )

(defn spacer-row []
  [:div
   (when (session/get :identity)
     [:div.row.justify-content-center
      [:div.col-4.pt-3.pb-3 ]])])

(defn greeting []
  [:h2 "Welcome to Opti-Life please sign in  :)"])

(defn modal [header body footer]
  [:div
   [:div.modal-dialog
    [:div.modal-content
     [:div.modal-header [:h3 header]]
     [:div.modal-body body]
     [:div.modal-footer [:div.bootstrap-dialog-footer footer]]]]
   [:div.modal-backdrop.fade.in]])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Base input functions
;; TODO: Reduce duplication of input code with inputs
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn input [type id placeholder fields]
  [:input.form-control.input-lg
   {:type        type
    :placeholder placeholder
    :value       (id @fields)
    :on-change   #(swap! fields assoc id (-> % .-target .-value))}])

(defn form-input [type label id placeholder fields optional?]
  [:div.form-group
   [:label label]
   (if optional?
     [input type id placeholder fields]
     [:div.input-group
      [input type id placeholder fields]
      [:span.input-group-addon " * "]])])

(defn text-input [label id placeholder fields & [optional?]]
  (form-input :text label id placeholder fields optional?))

(defn password-input [label id placeholder fields & [optional?]]
  (form-input :password label id placeholder fields optional?))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Select Component
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn create-select [label id placeholder fields items]
  [:select.custom-select.custom-select-md
   {:value (if (empty? @fields) "" (id @fields))
    :on-change #(swap! fields assoc id (-> % .-target .-value))}
   [:option {:value ""} placeholder]
   (for [item items]
     [:option {:value item} item])])

(defn select-input [label id placeholder fields items & [optional?]]
  [:div.form-group
   [:label label]
   (if optional?
     (create-select label id placeholder fields items)
     [:div.input-group
      (create-select label id placeholder fields items)
      [:span.input-group-addon " * "]])])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Input number
;; TODO: Reduce duplication of input code with inputs
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn input-number [type id placeholder fields]
  [:input.form-control.input-lg
   {:type        type
    :placeholder placeholder
    :value       (id @fields)
    :on-change   #(swap! fields assoc id (int (-> % .-target .-value)))}])

(defn form-number-input [type label id placeholder fields optional?]
  [:div.form-group
   [:label label]
   (if optional?
     [input-number type id placeholder fields]
     [:div.input-group
      [input-number type id placeholder fields]
      [:span.input-group-addon " * "]])])

(defn text-number [label id placeholder fields optional?]
  (form-number-input :number label id placeholder fields optional?))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Input number
;; TODO: Reduce duplication of input code with inputs
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn input-focus [type id placeholder fields]
  [:input.form-control.input-lg
   {:type        type
    :auto-focus true
    :placeholder placeholder
    :value       (id @fields)
    :on-change   #(swap! fields assoc id (-> % .-target .-value))}])

(defn form-input-focus [type label id placeholder fields optional?]
  [:div.form-group
   [:label label]
   (if optional?
     [input-focus type id placeholder fields]
     [:div.input-group
      [input-focus type id placeholder fields]
      [:span.input-group-addon " * "]])])

(defn text-input-focus [label id placeholder fields & [optional?]]
  (form-input-focus :text label id placeholder fields optional?))







