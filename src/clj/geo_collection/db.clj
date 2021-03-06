(ns geo-collection.db
  (:require [clojure.java.jdbc :refer :all]
            [clj-time.local :as tl]))

(def db
  {:classname "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname "db/geo.db"})

(defn create-db []
  (try (db-do-commands db
                       (create-table-ddl :locations
                                         [:description :text]
                                         [:time :text]
                                         [:longitude :real]
                                         [:latitude :real]
                                         [:accuracy :real]
                                         [:speed :real]))
       (catch Exception e (println e))))

(defn save-location [data]
  (let [time-now (tl/local-now)]
    (println "SAVING" data)
    (insert! db :locations (assoc data :time time-now))
    time-now))

(create-db)
