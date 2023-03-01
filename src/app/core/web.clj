(ns app.core.web
  "Freedom to ask. That's why we love the Internet. It is in this namespace where all the magic happens."
  (:require
    [app.core.config :as config]
    [cheshire.core :as json]
    [clj-http.client :as client]
    [clojure.walk :as walk]))


(defn save-get
  [uri opts]
  (try
    (client/get uri opts)
    (catch Exception e {:uri     uri
                        :opts    opts
                        :error   e
                        :message (:message e)})))


(defn read-api
  "Returns the body of the API response."
  [api-name tag]
  (when-let [{:keys [uri timeout tags opts]
              :or   {timeout 1000}} (get (config/get) api-name)]
    (-> uri
      (save-get {:cookie-policy :none
                 :socket-timeout timeout
                 :connection-timeout timeout
                 :accept :json
                 :query-params (into {tags tag} opts)})
      :body
      (json/parse-string))))


(defn read-api-in-order
  "Returns vector of maps, where each map is a response from the API."
  ([api-name tags]
   (read-api-in-order api-name tags {}))
  ([api-name tags {:as opts}]
   (let [{:keys [max-parallel, delay]
          :or   {max-parallel 1, delay 0}} (-> (config/get)
                                             (get api-name)
                                             (merge opts))
         queue (partition max-parallel max-parallel nil tags)
         read-api (partial read-api api-name)]
     (if (seq queue)
       (->> queue
         (map-indexed
           (fn [idx tags]
             (do
               (when (not= idx 0) (Thread/sleep delay))
               (if (> (count tags) 1)
                 (pmap read-api tags)
                 (map read-api tags)))))
         (flatten))
       {:error? true
        :message "Incorrect. Use URI-query like: `.../search?tag=<tag>..."}))))


(defn expand-tags
  "Returns map with a statistic for each tag."
  [{:keys [tags answer_count]}]
  (for [tag tags]
    {:tag tag
     :answered answer_count}))


(defn prepare-data
  "Returns a list of maps, where an each map is a statistic of answers."
  [data]
  (->> data
    (walk/keywordize-keys)
    (map :items)
    (flatten)
    (map expand-tags)
    (flatten)))


(defn get-statistic-by-tags
  "Returns a map, where the keys are the tags from the questions on the passed topic, and the values are the maps with statistic for answers."
  [api-name tags & {:as opts}]
  (let [data (read-api-in-order api-name tags opts)]
    (if (:error? data)
      data
      (->> data
        (prepare-data)
        (group-by :tag)
        (map (fn [[tag answers]]
               {tag {:total    (count answers)
                     :answered (->> answers (map :answered) (apply +))}}))
        (apply merge)))))
