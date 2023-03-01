(ns build
  (:require
    [clojure.tools.build.api :as api]))


(def basis (api/create-basis {:project "deps.edn"}))

(def version (format "0.0.%s" (api/git-count-revs nil)))

(def class-dir "target/classes")

(def jar-file (format "target/assigment-%s-standalone.jar" version))


(defn clean
  "Clean target directory."
  ([]
   (clean nil))
  ([_]
   (println (format "[build] Clean target directory."))
   (api/delete {:path "target"})))


(defn uberjar
  "Build the standalone uberjar of project."
  [opts]
  (clean)
  (api/copy-dir {:src-dirs ["src" "resources"]
                 :target-dir class-dir})
  (api/compile-clj {:basis basis
                    :src-dirs ["src"]
                    :class-dir class-dir})
  (api/uber {:class-dir class-dir
             :uber-file jar-file
             :basis basis
             :main 'app.main})
  (println (format "[build] Compiled uberjar file `%s`." jar-file)))
