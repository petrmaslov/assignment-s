(ns user
  "The default namespace for development mode."
  (:require
    [hashp.core]
    [clojure.tools.namespace.repl :as namespace]))

(println "Welcome to development mode.")

(namespace/disable-reload!)

(namespace/set-refresh-dirs "dev")
