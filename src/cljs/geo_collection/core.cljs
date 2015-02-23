(ns geo-collection.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [goog.events :as events]
              [goog.history.EventType :as EventType]
              [ajax.core :refer [POST]]
              [cljsjs.react :as react])
    (:import goog.History))

;; geo stuff

(defn get-position []
  (let [geo (.-geolocation js/navigator)]
    (.getCurrentPosition geo (fn [pos]
                               (let [coords (.-coords pos)]
                                 (.log js/console coords)
                                 (session/assoc-in! [:location] {:latitude (.-latitude coords)
                                                                 :longitude (.-longitude coords)
                                                                 :accuracy (.-accuracy coords)
                                                                 :speed (.-speed coords)}))))))

(defn handler [response]
  (.log js/console response))

(defn error-handler [{:keys [status status-text]}]
  (println "ERROR" status status-text))
;  (js/alert status-text))

(defn save-position []
  (POST "/save-position" {:params (session/get :location)
                          :handler handler
                          :error-handler error-handler}))
;; -------------------------
;; Views

(defn home-page []
  [:div {:class "container"}
   [:div {:class "row"}
    [:div {:class "col-xs-12"}
     [:button {:class "btn btn-success btn-block"
               :type :button
               :on-click get-position} "GET POSITION"]
     [:h4 (str "Latitude  " (session/get-in [:location :latitude] "-"))]
     [:h4 (str "Longitude " (session/get-in [:location :longitude] "-"))]
     [:h4 (str "Accuracy  " (session/get-in [:location :accuracy] "-"))]
     [:h4 (str "Speed     " (session/get-in [:location :speed] "-"))]
     [:input {:type :text
              :class "col-xs-12 form-control"}]
     [:button {:class "btn btn-primary btn-block"
               :type :button
               :on-click save-position} "Save Position"]
     ]]])

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn init! []
  (hook-browser-navigation!)
  (reagent/render-component [current-page] (.getElementById js/document "app")))
