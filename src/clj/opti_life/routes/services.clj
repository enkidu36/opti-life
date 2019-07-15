(ns opti-life.routes.services
  (:require [opti-life.routes.services.auth :as a]
            [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s])
  (:import (org.postgresql.core BaseResultSet)))


(s/defschema UserRegistration
  {:id String
   :pass String
   :pass-confirm String})

(s/defschema Result
  {:result s/Keyword
   (s/optional-key :message) String})

(defapi service-routes
  {:swagger {:ui "/swagger-ui"
             :spec "/swagger.json"
             :data {:info {:version "1.0.0"
                           :title "Opti-life API"
                           :description "Public services"}}}}
    (POST "/register" req
      :return      Result
      :body [user UserRegistration]
      :summary     "register a new user"
      (a/register! req user))

    (POST "/login" req
      :header-params [authorization :- String]
      :summary "log in the user and create a session"
      :return Result
      (a/login! req authorization))

    (POST "/logout" []
      :summary "remove user session"
      :return Result
      (a/logout!)))
