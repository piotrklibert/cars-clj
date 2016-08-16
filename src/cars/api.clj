(ns cars.api
  (:require [liberator.core :refer [resource defresource]]
            [ring.middleware.params :refer [wrap-params]]
            [compojure.core :refer [defroutes ANY context]]
            [net.cgrand.enlive-html :as html]
            [cars.main]
            [clojure.string :as str]
            [compojure.route :as route]
            [cars.util :refer :all]
            [clj-http.client :as client])
  (:import [java.io StringReader]))


(def get-data
  (comp->
   (juxt (selector [:td :table.item :th])
         (selector [:td :table.item :.value :strong])
         (selector [:#offerdescription :div :div :div :div :img])
         (get-text [:#offerdescription :#textContent  :p]))
   (juxt (fn [ [a b _ _] ]
           (->> (map html/texts [a b])
                (apply interleave)
                (apply hash-map)))
         (fn [ [_ _ c d] ]
           {:img (-> c first :attrs :src str)
            :desc (str  d)}))
   (partial apply merge)))


(def fetch*
  (memoize
   (fn [url]
     (-> url fetch-page+parse get-data))))

(defn fetch [ctx]
  (fetch* (get-in ctx [:request :params "url"])))


(defroutes app
  (route/files "/" {:root "resources"})
  (ANY ["/api/fetch/"] []
       (wrap-params (resource :available-media-types ["application/json"]
                              :handle-ok (wrap-params fetch))))

  (ANY "/api/" []
       (resource :available-media-types ["application/json"]
                 :handle-ok (fn [ctx] (cars.main/main)))))


(def handler
  (-> app
      wrap-params))
