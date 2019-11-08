(ns opti-life.core
  (:require [reagent.core :as r]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [opti-life.ajax :refer [load-interceptors!]]
            [ajax.core :refer [GET POST]]
            [opti-life.pages.home-page :as home]
            [opti-life.pages.food-page :as food]
            [opti-life.pages.plan-page :as plan]
            [opti-life.pages.scroll-page :as scroll]
            [opti-life.components.common :as c]
            [opti-life.components.registration :as reg]
            [opti-life.components.login :as l])
  (:import goog.History))

(defn nav-link [uri title page collapsed?]
  [:li.nav-item
   {:class (when (= page (session/get :page)) "active")}
   [:a.nav-link
    {:href uri
     :on-click #(reset! collapsed? true)} title]])

(defn user-menu []
  (if-let [id (session/get :identity)]
    [:ul.nav.justify-content-end
     [:li.nav-item
      [:a.dropdown-item.btn {:on-click #(POST
                                          "/logout"
                                          {:handler (fn [] (session/remove! :identity))})}
       [:i.fa.fa-user] " " id " | sign out"]]]
    [:ul.nav.justify-content-end
     [:li.nav-item [l/login-button]]
     [:li.nav-item [reg/registration-button]]]))

(defn navbar []
  (let [collapsed? (r/atom false)]
    (fn []
      [:nav.navbar.navbar-light.bg-faded
        [:button.navbar-toggler {
                                             :on-click    #(swap! collapsed? not)
                                             :data-toggle "collapse"
                                             :data-target "#col-nav"} "☰"]
       [:div#col-nav.collapse.navbar-collapse-xs
        [:a.navbar-brand {:href "#/"} "Opti-Life"]
        [:ul.nav.justify-content-center
         [nav-link "#/" "Home" :home collapsed?]
         [nav-link "#/about" "About" :about collapsed?]]]
       [user-menu]])))


(defn about-page []
  [:div.container
   [:div.row
    [:div.col-md-12
     "this is the story of opti-life... work in progress"]]])

(defn scroll-page []
  [:div.container
   [:div.row
    [:div.col-md-12
     "scroll page"]]])

(defn modal []
  (when-let [session-modal (session/get :modal)]
    [session-modal]))


(def pages
  {:home #'home/page
   :food #'food/page
   :plan #'plan/page
   :scroll #'scroll/page
   :about #'about-page})

(defn page []
  [:div.container-fluid.pr-0.pl-0
   [modal]
  [(pages (session/get :page))]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :page :home))

(secretary/defroute "/food" []
                    (session/put! :page :food))

(secretary/defroute "/plan" []
                    (session/put! :page :plan))

(secretary/defroute "/about" []
                    (session/put! :page :about))

(secretary/defroute "/scroll" []
                    (session/put! :page :scroll))


;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
        (events/listen
          HistoryEventType/NAVIGATE
          (fn [event]
              (secretary/dispatch! (.-token event))))
        (.setEnabled true)))

;; -------------------------
;; Initialize app

(defn mount-components []
  (r/render [#'navbar] (.getElementById js/document "navbar"))
  (r/render [#'page] (.getElementById js/document "app"))
  )

(defn init! []
  (load-interceptors!)
  (hook-browser-navigation!)
  (session/put! :identity js/identity)
  (mount-components))
