(ns app.core.server
  "Web server."
  (:require
    [ring.adapter.jetty :as jetty]))


(defonce server (atom nil))


(defn any-url
  "Any URL is perfect."
  [request]
  {:status 200
   :headers {"Content-Type" "text/html;charset=utf-8"}
   :body "Hello!"})


(defn start-server!
  "Runs Jetty server."
  [{:keys [port]
    :or   {port 8080}
    :as opts}]
  (when (nil? @server)
    (reset! server (jetty/run-jetty #'any-url {:port port, :join? false}))
    (println (format "[server] Started on port %s." port))))


(defn stop-server!
  "Stops runned Jetty server."
  []
  (when @server
    (.stop @server)
    (reset! server nil)
    (println "[server] Stopped and released the port.")))
