(ns user
  (:require [mount.core :as mount]
            [opti-life.figwheel :refer [start-fw stop-fw cljs]]
            opti-life.core))

(defn start []
  (mount/start-without #'opti-life.core/repl-server))

(defn stop []
  (mount/stop-except #'opti-life.core/repl-server))

(defn restart []
  (stop)
  (start))


