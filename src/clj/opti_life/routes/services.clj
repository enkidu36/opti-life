(ns opti-life.routes.services
  (:require [opti-life.routes.services.auth :as a]
            [opti-life.routes.services.food :as food]
            [opti-life.routes.services.user-test :as user-test]
            [opti-life.routes.services.file-upload :refer [handle-upload fetch-user-tests]]
            [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]))


(s/defschema UserRegistration
  {:id           String
   :pass         String
   :pass-confirm String})

(s/defschema Food
  {(s/optional-key :id) s/Int
   :name s/Str
   :food_category s/Str})

(s/defschema UserTest
  {(s/optional-key :id) s/Int
   :user_id s/Str
   :admin_date s/Inst
   :file_type s/Str})

(s/defschema Result
  {:result                   s/Keyword
   (s/optional-key :message) String})

(defapi service-routes
  {:swagger {:ui   "/swagger-ui"
             :spec "/swagger.json"
             :data {:info {:version     "1.0.0"
                           :title         "Opti-life API"
                           :description "Public services"}}}}
  (POST "/add-food" []
    :return Result
    :body [food Food]
    :summary "Add a new food"
    (food/add-food food))

  (GET "/all-foods" []
       :summary "Retrieve all foods"
       :return [Food]
    (food/all-foods))

  (GET "/fetch-tests" req
       :summary "Retrieves all foods for user"
        :return [UserTest]
       (fetch-user-tests req))

  (POST "/upload" req
    (handle-upload req))

  (POST "/register" req
    :return Result
    :body [user UserRegistration]
    :summary "register a new user"
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