(ns opti-life.components.scroll
  (:require [reagent.core :as r]))

(defn- get-scroll-top []
  (if (exists? (.-pageYOffset js/window))
    (.-pageYOffset js/window)
    (.-scrollTop (or (.-documentElement js/document)
                     (.-parentNode (.-body js/document))
                     (.-body js/document)))))

(defn- get-el-top-position [node]
  (js/console.log (.getElementById js/document "plan-cal"))
  (if (not node)
    0
    (+ (.-offsetTop node) (get-el-top-position (.-offsetParent node)))))

(defn- safe-component-mounted? [component]
  (try (boolean (r/dom-node component)) (catch js/Object _ false)))

(defn debounce
  "Returns a function that will call f only after threshold has passed without new calls
  to the function. Calls prep-fn on the args in a sync way, which can be used for things like
  calling .persist on the event object to be able to access the event attributes in f"
  ([threshold f] (debounce threshold f (constantly nil)))
  ([threshold f prep-fn]
   (let [t (atom nil)]
     (fn [& args]
       (when @t (js/clearTimeout @t))
       (apply prep-fn args)
       (reset! t (js/setTimeout #(do
                                   (reset! t nil)
                                   (apply f args))
                                threshold))))))

(defn container-el [id]
  (js/console.log (str "test" (.getElementById js/document id)))
  (if id
    (.getElementById js/document id)
    js/window))

(defn scroll-plus-offset [el]

  (+ (.-scrollTop el) (.-offsetHeight el)))

(defn infinite-scroll [props]
  ;; props is a map with :can-show-more? & :load-fn keys
  (let [listener-fn (atom nil)
        id (:container-id props)
        detach-scroll-listener (fn []
                                 (when @listener-fn
                                   (.removeEventListener (container-el id) "scroll" @listener-fn)
                                   ;(.removeEventListener (container-el id) "resize" @listener-fn)
                                   (reset! listener-fn nil)))
        should-load-more? (fn [this]
                            (let [node (r/dom-node this)
                                  scroll-top (get-scroll-top)
                                  my-top (get-el-top-position node)
                                  threshold 50]
                               (set! (.-innerHTML (container-el "scroll-height")) (str "Scroll height: " (.-scrollHeight (container-el id))))
                               (set! (.-innerHTML (container-el "scroll-position")) (str "Position: "(scroll-plus-offset (container-el id))))
                               (set! (.-innerHTML (container-el "scroll-top")) (str "Scroll top: "(.-scrollTop (container-el id))))
                               (or
                                 (= (.-scrollHeight (container-el id)) (scroll-plus-offset (container-el id)))
                                 (< (.-scrollTop (container-el id)) threshold))))
        scroll-listener (fn [this]
                          (when (safe-component-mounted? this)
                            (let [{:keys [load-fn can-show-more?]} (r/props this)]
                              (when (and can-show-more?
                                         (should-load-more? this))
                                (println "loading more...")
                                (detach-scroll-listener)
                                (load-fn)))))
        debounced-scroll-listener (debounce 200 scroll-listener)
        attach-scroll-listener (fn [this]
                                 (let [{:keys [can-show-more?]} (r/props this)]
                                   (when can-show-more?
                                     (when-not @listener-fn
                                       (reset! listener-fn (partial debounced-scroll-listener this))
                                       (.addEventListener (container-el id) "scroll" @listener-fn)  ))))]
    (r/create-class
      {:component-did-mount
       (fn [this]
         (attach-scroll-listener this)
         (-> js/document
             (.querySelector "#plan-cal")
             (.-scrollTop)
             (set! 330)))
       :component-did-update
       (fn [this _]
         (attach-scroll-listener this))
       :component-will-unmount
       detach-scroll-listener
       :reagent-render
       (fn [props]
         [:div])})))
