(ns app.main
  "The default namespace for a project."
  (:gen-class)
  (:require
    [app.core.server :as server]))


(defn -main
  [& {:as opts}]
  (server/start-server! opts))
