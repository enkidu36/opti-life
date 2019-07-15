(ns opti-life.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[opti-life started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[opti-life has shut down successfully]=-"))
   :middleware identity})
