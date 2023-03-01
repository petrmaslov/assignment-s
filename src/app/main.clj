(ns app.main
  "The default namespace for a project."
  (:gen-class)
  (:require
    [app.core.config :as config]
    [app.core.server :as server]))


(defn -main
  "The main function of the project. It runs from command-line with options or not."
  [& {:as opts}]
  (config/load (:config opts))
  (server/start-server! opts))
