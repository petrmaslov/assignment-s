(ns app.core.web
  "Freedom to ask. That's why we love the Internet. It is in this namespace where all the magic happens.")


(defn get-statistic-by-tags
  "Returns a map, where the keys are the tags from the questions on the passed topic, and the values are the maps with statistic for answers."
  [api-name tags & {:as opts}]
  {:api-name api-name
   :tags tags
   :opts opts})
