(ns app.core.config-test
  (:require
    [app.core.config :as SUT]))


(defn fixture-with-test-config
  "Fixture. Loads tested configuration."
  [test]
  (with-redefs [app.core.config/default-config "test/config.edn"]
    (SUT/load)
    (test)))
