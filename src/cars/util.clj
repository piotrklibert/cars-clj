(ns cars.util
  (:require [clj-http.client :as client]
            [net.cgrand.enlive-html :as html]
            [clojure.pprint :refer [pprint]]
            [clojure.reflect :refer [reflect]]
            [clojure.repl :refer [apropos dir doc find-doc pst source]]
            [clojure.set :as set]
            [clojure.string :as str]
            [clojure.test :as test]
            [clojure.tools.namespace.repl :refer [refresh refresh-all]])
    (:import [java.io StringReader]))

(defn fetch-page+parse [url]
  (time
   (-> url client/get :body StringReader. html/html-resource)))

(defn flip
  ([f] (fn [a b & rest] (apply f b a rest)))
  ([f x] (partial (flip f) x)))

(defn comp-> [& more]
  (apply comp (reverse more)))

(defn selector [sel]
  (fn [nodes]
    (vec (html/select nodes sel))))

(defn get-text [sel]
  (comp->
   (selector sel)
   html/texts
   str/join
   str/trim))

(defn get-attr [sel kws]
  (comp (flip get-in kws)
        (selector sel)))
