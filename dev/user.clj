(ns user
  "The default namespace for development mode."
  (:require
    ;; WARNING: Order of requires here is important!
    [hashp.core]
    [clojure.tools.namespace.repl :as namespace]
    [app.main :as main]))


(println "Welcome to development mode.")

(namespace/disable-reload!)

(namespace/set-refresh-dirs "src" "dev")


(main/-main)
