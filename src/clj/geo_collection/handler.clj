(ns geo-collection.handler
  (:require [compojure.core :refer [GET POST defroutes routes]]
            [compojure.route :refer [not-found resources]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [selmer.parser :refer [render-file]]
            [prone.middleware :refer [wrap-exceptions]]
            [environ.core :refer [env]]
            [ring.middleware.transit :refer [wrap-transit-body]]))

(defroutes api-routes
  (POST "/save-position" request (println "REQ:" request)
        "SUCCESS"))

(defroutes web-routes
  (GET "/" [] (render-file "templates/index.html" {:dev (env :dev?)}))
  (resources "/")
  (not-found "Not Found"))

(def app
  (let [handler (routes
                 (wrap-transit-body api-routes {:keywords? true})
                 (wrap-defaults web-routes site-defaults))]
    (if (env :dev?) (wrap-exceptions handler) handler)))
