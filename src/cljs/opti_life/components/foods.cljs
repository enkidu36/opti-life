(ns opti-life.components.foods
  (:require [reagent.core :refer [atom]]
            [reagent.session :as session]
            [clojure.string :as string]
            [ajax.core :refer [POST GET]]
            [opti-life.components.common :as c]))

