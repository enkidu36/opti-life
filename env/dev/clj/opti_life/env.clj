(ns opti-life.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [opti-life.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[opti-life started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[opti-life has shut down successfully]=-"))
   :middleware wrap-dev})
