(ns opti-life.components.date-util
  (:require [cljs-time.format :as f]
            [cljs-time.core :as t]
            [cljs-time.predicates :as predicate]))

(def days ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"])

(defn get-today []
  (t/today-at-midnight))

(defn plus-day [dt count]
  (t/plus dt (t/days count)))

(defn equal-date? [this that]
  "Compare the date parts of datetime objects"
  (let [this-date (t/at-midnight this)
        that-date (t/at-midnight that)]
    (t/equal? this-date that-date)))

(defn date->string [date, format]
  (f/unparse (f/formatter format) date))

(defn first-day-of-month? [date]
  (= (date->string date "d") "1"))

(defn last-monday [date]
  (if (predicate/monday? date)
    date                                                    ; It is already Monday
    (let [today-week-day (t/day-of-week date)
          offset (- today-week-day 1)]
      (t/minus date (t/days offset)))))

(defn build-calendar-dates []
  (let [today (t/today)
        last-monday (last-monday today)
        start-date (t/minus last-monday (t/days 14))]
    (map #(t/plus start-date (t/days %)) (range 43))))

(defn build-week [first-day]
  (map #(t/plus first-day (t/days %)) (range 7)))

(defn build-weeks [start-date count]
  (map #(t/plus start-date (t/days (* 7 %))) (range count)))

(defn day-with-new-month [date-time]
  (let [day (date->string date-time "d")]
    (if (= day "1")
      (str (date->string date-time "MMM") " " day)
      day)))

(def db-back [{:date (t/local-date 2019 10 21) :meals [{:name "breakfast" :foods [{:name "orange"} {:name "oatmeal"}]}
                                                       {:name "lunch" :foods [{:name "chicken"} {:name "cheese"}]}
                                                       {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}

              {:date (t/local-date 2019 10 22) :meals [{:name "breakfast" :foods [{:name "bannas"} {:name "oatmeal"}]}
                                                       {:name "lunch" :foods [{:name "turkey"} {:name "cheese"}]}
                                                       {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
              {:date (t/local-date 2019 10 23) :meals [{:name "breakfast" :foods [{:name "orange"} {:name "oatmeal"}]}
                                                       {:name "lunch" :foods [{:name "hamburger"} {:name "cheese"}]}
                                                       {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
              {:date (t/local-date 2019 10 24) :meals [{:name "breakfast" :foods [{:name "tomato"} {:name "oatmeal"}]}
                                                       {:name "lunch" :foods [{:name "potato"} :name "cheese"]}
                                                       {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
              {:date (t/local-date 2019 10 25) :meals [{:name "breakfast" :foods [{:name "apple"} {:name "oatmeal"}]}
                                                       {:name "lunch" :foods [{:name "jimi johns"} :name "cheese"]}
                                                       {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
              {:date (t/local-date 2019 10 26) :meals [{:name "breakfast" :foods [{:name "raisins"} {:name "oatmeal"}]}
                                                       {:name "lunch" :foods [{:name "macaroni"} :name "cheese"]}
                                                       {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
              {:date (t/local-date 2019 10 27) :meals [{:name "breakfast" :foods [{:name "apricots"} {:name "oatmeal"}]}
                                                       {:name "lunch" :foods [{:name "macaroni"} :name "cheese"]}
                                                       {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}])


(def db [{:date (t/local-date 2019 10 28) :meals [{:name "breakfast" :foods [{:name "apple"} {:name "apple"}]}
                                                  {:name "lunch" :foods [{:name "macaroni"} {:name "cheese"}]}
                                                  {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
         {:date (t/local-date 2019 10 29) :meals [{:name "breakfast" :foods [{:name "orange"} {:name "oatmeal"}]}
                                                  {:name "lunch" :foods [{:name "macaroni"} {:name "cheese"}]}
                                                  {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
         {:date (t/local-date 2019 10 30) :meals [{:name "breakfast" :foods [{:name "raisins"} {:name "oatmeal"}]}
                                                  {:name "lunch" :foods [{:name "macaroni"} {:name "cheese"}]}
                                                  {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
         {:date (t/local-date 2019 10 31) :meals [{:name "breakfast" :foods [{:name "peach"} {:name "oatmeal"}]}
                                                  {:name "lunch" :foods [{:name "macaroni"} :name "cheese"]}
                                                  {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
         {:date (t/local-date 2019 11 1) :meals [{:name "breakfast" :foods [{:name "apricots"} {:name "oatmeal"}]}
                                                 {:name "lunch" :foods [{:name "macaroni"} :name "cheese"]}
                                                 {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
         {:date (t/local-date 2019 11 2) :meals [{:name "breakfast" :foods [{:name "bananas"} {:name "oatmeal"}]}
                                                 {:name "lunch" :foods [{:name "macaroni"} :name "cheese"]}
                                                 {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
         {:date (t/local-date 2019 11 3) :meals [{:name "breakfast" :foods [{:name "dates"} {:name "oatmeal"}]}
                                                 {:name "lunch" :foods [{:name "macaroni"} :name "cheese"]}
                                                 {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
         {:date (t/local-date 2019 11 4) :meals [{:name "breakfast" :foods [{:name "peach"} {:name "oatmeal"}]}
                                                 {:name "lunch" :foods [{:name "macaroni"} {:name "cheese"}]}
                                                 {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
         {:date (t/local-date 2019 11 5) :meals [{:name "breakfast" :foods [{:name "grapes"} {:name "oatmeal"}]}
                                                 {:name "lunch" :foods [{:name "macaroni"} {:name "cheese"}]}
                                                 {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
         {:date (t/local-date 2019 11 6) :meals [{:name "breakfast" :foods [{:name "grapes"} {:name "oatmeal"}]}
                                                 {:name "lunch" :foods [{:name "macaroni"} :name "cheese"]}
                                                 {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
         {:date (t/local-date 2019 11 7) :meals [{:name "breakfast" :foods [{:name "grapes"} {:name "oatmeal"}]}
                                                 {:name "lunch" :foods [{:name "macaroni"} :name "cheese"]}
                                                 {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
         {:date (t/local-date 2019 11 8) :meals [{:name "breakfast" :foods [{:name "grapes"} {:name "oatmeal"}]}
                                                 {:name "lunch" :foods [{:name "macaroni"} {:name "cheese"}]}
                                                 {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
         {:date (t/local-date 2019 11 9) :meals [{:name "breakfast" :foods [{:name "grapes"} {:name "oatmeal"}]}
                                                 {:name "lunch" :foods [{:name "macaroni"} {:name "cheese"}]}
                                                 {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
         {:date (t/local-date 2019 11 10) :meals [{:name "breakfast" :foods [{:name "grapes"} {:name "oatmeal"}]}
                                                  {:name "lunch" :foods [{:name "macaroni"} {:name "cheese"}]}
                                                  {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
         {:date (t/local-date 2019 11 11) :meals [{:name "breakfast" :foods [{:name "grapes"} {:name "oatmeal"}]}
                                                  {:name "lunch" :foods [{:name "macaroni"} {:name "cheese"}]}
                                                  {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
         {:date (t/local-date 2019 11 12) :meals [{:name "breakfast" :foods [{:name "grapes"} {:name "oatmeal"}]}
                                                  {:name "lunch" :foods [{:name "macaroni"} {:name "cheese"}]}
                                                  {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
         {:date (t/local-date 2019 11 13) :meals [{:name "breakfast" :foods [{:name "grapes"} {:name "oatmeal"}]}
                                                  {:name "lunch" :foods [{:name "macaroni"} {:name "cheese"}]}
                                                  {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
         {:date (t/local-date 2019 11 14) :meals [{:name "breakfast" :foods [{:name "grapes"} {:name "oatmeal"}]}
                                                  {:name "lunch" :foods [{:name "macaroni"} {:name "cheese"}]}
                                                  {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
         {:date (t/local-date 2019 11 15) :meals [{:name "breakfast" :foods [{:name "grapes"} {:name "oatmeal"}]}
                                                  {:name "lunch" :foods [{:name "macaroni"} {:name "cheese"}]}
                                                  {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
         {:date (t/local-date 2019 11 16) :meals [{:name "breakfast" :foods [{:name "grapes"} {:name "oatmeal"}]}
                                                  {:name "lunch" :foods [{:name "macaroni"} {:name "cheese"}]}
                                                  {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
         {:date (t/local-date 2019 11 17) :meals [{:name "breakfast" :foods [{:name "grapes"} {:name "oatmeal"}]}
                                                  {:name "lunch" :foods [{:name "macaroni"} {:name "cheese"}]}
                                                  {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
         {:date (t/local-date 2019 11 18) :meals [{:name "breakfast" :foods [{:name "grapes"} {:name "oatmeal"}]}
                                                  {:name "lunch" :foods [{:name "macaroni"} {:name "cheese"}]}
                                                  {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
         {:date (t/local-date 2019 11 19) :meals [{:name "breakfast" :foods [{:name "grapes"} {:name "oatmeal"}]}
                                                  {:name "lunch" :foods [{:name "macaroni"} {:name "cheese"}]}
                                                  {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
         {:date (t/local-date 2019 11 20) :meals [{:name "breakfast" :foods [{:name "grapes"} {:name "oatmeal"}]}
                                                  {:name "lunch" :foods [{:name "macaroni"} {:name "cheese"}]}
                                                  {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
         {:date (t/local-date 2019 11 21) :meals [{:name "breakfast" :foods [{:name "grapes"} {:name "oatmeal"}]}
                                                  {:name "lunch" :foods [{:name "macaroni"} {:name "cheese"}]}
                                                  {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
         {:date (t/local-date 2019 11 22) :meals [{:name "breakfast" :foods [{:name "grapes"} {:name "oatmeal"}]}
                                                  {:name "lunch" :foods [{:name "macaroni"} {:name "cheese"}]}
                                                  {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
         {:date (t/local-date 2019 11 23) :meals [{:name "breakfast" :foods [{:name "grapes"} {:name "oatmeal"}]}
                                                  {:name "lunch" :foods [{:name "macaroni"} {:name "cheese"}]}
                                                  {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
         {:date (t/local-date 2019 11 24) :meals [{:name "breakfast" :foods [{:name "grapes"} {:name "oatmeal"}]}
                                                  {:name "lunch" :foods [{:name "macaroni"} {:name "cheese"}]}
                                                  {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
         {:date (t/local-date 2019 11 25) :meals [{:name "breakfast" :foods [{:name "grapes"} {:name "oatmeal"}]}
                                                  {:name "lunch" :foods [{:name "macaroni"} {:name "cheese"}]}
                                                  {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
         {:date (t/local-date 2019 11 26) :meals [{:name "breakfast" :foods [{:name "grapes"} {:name "oatmeal"}]}
                                                  {:name "lunch" :foods [{:name "macaroni"} {:name "cheese"}]}
                                                  {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
         {:date (t/local-date 2019 11 27) :meals [{:name "breakfast" :foods [{:name "grapes"} {:name "oatmeal"}]}
                                                  {:name "lunch" :foods [{:name "macaroni"} {:name "cheese"}]}
                                                  {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
         {:date (t/local-date 2019 11 28) :meals [{:name "breakfast" :foods [{:name "grapes"} {:name "oatmeal"}]}
                                                  {:name "lunch" :foods [{:name "macaroni"} {:name "cheese"}]}
                                                  {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
         {:date (t/local-date 2019 11 29) :meals [{:name "breakfast" :foods [{:name "grapes"} {:name "oatmeal"}]}
                                                  {:name "lunch" :foods [{:name "macaroni"} {:name "cheese"}]}
                                                  {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
         {:date (t/local-date 2019 11 30) :meals [{:name "breakfast" :foods [{:name "grapes"} {:name "oatmeal"}]}
                                                  {:name "lunch" :foods [{:name "macaroni"} {:name "cheese"}]}
                                                  {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
         {:date (t/local-date 2019 12 01) :meals [{:name "breakfast" :foods [{:name "grapes"} {:name "oatmeal"}]}
                                                  {:name "lunch" :foods [{:name "macaroni"} {:name "cheese"}]}
                                                  {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}])

(def db-forward [{:date (t/local-date 2019 12 2) :meals [{:name "breakfast" :foods [{:name "prunes"} {:name "oatmeal"}]}
                                                         {:name "lunch" :foods [{:name "macaroni"} {:name "cheese"}]}
                                                         {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
                 {:date (t/local-date 2019 12 3) :meals [{:name "breakfast" :foods [{:name "oranges"} {:name "oatmeal"}]}
                                                         {:name "lunch" :foods [{:name "macaroni"} {:name "cheese"}]}
                                                         {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
                 {:date (t/local-date 2019 12 4) :meals [{:name "breakfast" :foods [{:name "rice"} {:name "oatmeal"}]}
                                                         {:name "lunch" :foods [{:name "macaroni"} {:name "cheese"}]}
                                                         {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
                 {:date (t/local-date 2019 12 5) :meals [{:name "breakfast" :foods [{:name "grapes"} {:name "oatmeal"}]}
                                                         {:name "lunch" :foods [{:name "macaroni"} :name "cheese"]}
                                                         {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
                 {:date (t/local-date 2019 12 6) :meals [{:name "breakfast" :foods [{:name "oatmeal"} {:name "oatmeal"}]}
                                                         {:name "lunch" :foods [{:name "macaroni"} :name "cheese"]}
                                                         {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
                 {:date (t/local-date 2019 12 7) :meals [{:name "breakfast" :foods [{:name "grapes"} {:name "oatmeal"}]}
                                                         {:name "lunch" :foods [{:name "macaroni"} :name "cheese"]}
                                                         {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}
                 {:date (t/local-date 2019 12 8) :meals [{:name "breakfast" :foods [{:name "grapes"} {:name "oatmeal"}]}
                                                         {:name "lunch" :foods [{:name "macaroni"} :name "cheese"]}
                                                         {:name "dinner" :foods [{:name "steak"} {:name "taters"}]}]}])


