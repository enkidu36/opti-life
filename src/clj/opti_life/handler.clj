(ns opti-life.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [opti-life.layout :refer [error-page]]
            [opti-life.routes.home :refer [home-routes]]
            [opti-life.routes.services :refer [service-routes]]
            [compojure.route :as route]
            [opti-life.env :refer [defaults]]
            [mount.core :as mount]
            [opti-life.middleware :as middleware]))

(mount/defstate init-app
                :start ((or (:init defaults) identity))
                :stop  ((or (:stop defaults) identity)))

(def app-routes
  (routes
    #'service-routes
    (-> #'home-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats))
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "page not found"})))))


(defn app [] (middleware/wrap-base #'app-routes))
