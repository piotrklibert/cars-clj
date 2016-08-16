(ns cars.main
  (:require [clj-http.client :as client]
            [net.cgrand.enlive-html :as html]
            [clojure.pprint :refer [pprint]]
            [clojure.reflect :refer [reflect]]
            [clojure.repl :refer [apropos dir doc find-doc pst source]]
            [clojure.set :as set]
            [clojure.string :as str]
            [clojure.test :as test]
            [cars.util :refer :all]
            [clojure.tools.namespace.repl :refer [refresh refresh-all]])
)


(defrecord Offer [price title url when where])

(def offer ->Offer)


(defn get-link [node]
  (let [getter (juxt (get-text [:a.detailsLink])
                     (get-attr [:a.detailsLink] [0 :attrs :href ])
                     (get-text [:a.detailsLinkPromoted])
                     (get-attr [:a.detailsLinkPromoted] [0 :attrs :href]))
        [a b c d] (getter node)]
    (if b [a b] [c d])))


(defn scrap [page]
  (let [nodes (html/select page [:#offers_table :td.offer])]
    (->> nodes
         (into [] (comp
                   (map (juxt (get-text [:p.price])
                              get-link
                              (get-text [:p.color-9.lheight16.marginbott5.x-normal])
                              (get-text [:p :small :span])))
                   (map flatten)
                   (map (partial apply  ->Offer)))))))


(def ^:dynamic *url*
  (str "http://olx.pl/motoryzacja/samochody/warszawa/"
       "?search%5Bfilter_float_price%3Ato%5D=8000"
       "&search%5Bfilter_enum_condition%5D%5B0%5D=notdamaged"
       "&search%5Bfilter_enum_transmission%5D%5B0%5D=automatic"
       "&search%5Bfilter_enum_country_origin%5D%5B0%5D=pl"
       "&search%5Bdist%5D=50"
       "&page="))


(defn main []
  (->> (str *url* 1)
       fetch-page+parse scrap
       (take 10)))
