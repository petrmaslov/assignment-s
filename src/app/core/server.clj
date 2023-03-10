(ns app.core.server
  "Web server."
  (:require
    [app.core.config :as config]
    [app.core.web :as web]
    [bidi.ring :refer [make-handler]]
    [cheshire.core :as json]
    [ring.adapter.jetty :as jetty]
    [ring.middleware.keyword-params :refer [wrap-keyword-params]]
    [ring.middleware.params :refer [wrap-params]]))


(defonce server (atom nil))


(defn stackoverflow-api-search
  "The once handler of service. It passes request and returns response's map."
  [request]
  {:status  200
   :headers {"Content-type" "application/json;charset=utf-8"}
   :body    (let [tags (:tag (:params request))
                  tags (if (string? tags) [tags] tags)]
              (-> :stackoverflow
                (web/get-statistic-by-tags tags)
                (json/generate-string {:pretty true})))})


(defn not-found
  "To have or not to have. It's when not to have."
  [_]
  {:status 404
   :headers {"Content-Type" "text/html;charset=utf-8"}
   :body "404. Not found."})


(def ^{:private true
       :doc "Routes of application."}
  routes
  ["/" {"search" {:get #'stackoverflow-api-search}
        true #'not-found}])


(def ^{:private true
       :doc "Application is routes plus Ring's middlewares."}
  app
  (-> (make-handler routes)
    ;; WARNING: The order of widdlewares here is important!
    wrap-keyword-params
    wrap-params))


(defn start-server!
  "Runs Jetty server."
  [{:as opts}]
  (when (nil? @server)
    (let [{:keys [port] :as options} (merge
                                       {:port  (:port (config/get))
                                        :join? false}
                                       opts)]
      (reset! server (jetty/run-jetty #'app options))
      (println (format "[server] Started on port %s." port)))))


(defn stop-server!
  "Stops runned Jetty server."
  []
  (when @server
    (.stop @server)
    (reset! server nil)
    (println "[server] Stopped and released the port.")))
