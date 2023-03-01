(ns app.core.fake-api
  (:require
    [app.core.config :as config]
    [app.core.server :as SUT]
    [cheshire.core :as json]
    [clojure.edn :as edn]
    [clojure.test :refer [use-fixtures deftest testing is]]
    [ring.adapter.jetty :as jetty]
    [ring.middleware.keyword-params :refer [wrap-keyword-params]]
    [ring.middleware.params :refer [wrap-params]]))


(defn- fake-response
  "Generate for fake API a fake response with used keys only."
  [prefix]
  (->> ["A" "B" "C"]
    (map-indexed
      (fn [idx suffix]
        {:tags [(format "tag-%s" suffix)]
         :answer_count idx}))
    (vec)))


(defn- fake-api-200-handler
  [{:keys [uri params]}]
  {:status 200
   :headers {"Content-Type" "application/json;charset=utf-8"}
   :body (let [tags (get-in (config/get) [:fake-api :tags])
               tags (get params (keyword tags))]
           (json/generate-string
             {:items (fake-response tags)}))})


(defn- fake-api-404-handler
  [{:keys [uri params]}]
  {:status 404
   :headers {"Content-Type" "application/json;charset=utf-8"}
   :body (json/generate-string {:error 404})})


(def routes
  ["/" {"search" #'fake-api-200-handler
        true     #'fake-api-404-handler}])


(def ^{:private true
       :doc "Fake API as Ring's application. As fake Ring's brotherhood."}
  fake-api
  (-> routes
    bidi.ring/make-handler
    wrap-keyword-params
    wrap-params))


(def ^:dynamic *api-server* nil)


(defn fixture-fake-api-server
  "Fixture. Runs a test API server for runned test.
   WARNING! We should loading test configuration before."
  [test]
  (binding [*api-server* (jetty/run-jetty fake-api {:port (:port (config/get)), :join? false})]
    (Thread/sleep 2000)
    (test)
    (Thread/sleep 1000)
    (.stop *api-server*)))
