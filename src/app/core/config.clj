(ns app.core.config
  "Configuration of the project."
  (:refer-clojure :exclude [get load])
  (:require
    [clojure.edn :as edn]
    [clojure.java.io :as io]))


(defonce ^:private configuration (atom nil))

(def default-config "resources/config.edn")


(defn save-slurp
  "Slurp that suppresses exceptions. Returns `nil` if the read file doesn't exist."
  [file]
  (try
    (edn/read-string (slurp (io/file file)))
    (catch Exception _ nil)))


(defn load
  "Reads passed a configuration's edn-file or default configuration file and writes result into configuration's atom or returns `nil`."
  ([]
   (load default-config))
  ([file]
   (if-let [config (save-slurp file)]
     (do
       (println (format "[config] Load from %s" file))
       (reset! configuration (merge config {:file file}))
       {:loaded? true, :file file})
     (load))))


(defn get
  "Returns actual a project's configuration."
  [& opt]
  (if @configuration
    @configuration
    (do
      (load)
      @configuration)))
