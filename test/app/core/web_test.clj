(ns app.core.web-test
  (:require
    [app.core.config-test :refer [fixture-with-test-config]]
    [app.core.fake-api :refer [fixture-fake-api-server *api-server*]]
    [app.core.web :as SUT]
    [clj-http.client :as client]
    [clojure.test :refer [use-fixtures deftest testing is]]))


(use-fixtures :once
  fixture-with-test-config
  fixture-fake-api-server)


(deftest get-statistic-by-tags
  (testing "Returns map where keys are tags from answers and values are statistic for it."
    (testing "for one tag"
      (is (= {"tag-A" {:answered 0, :total 1}
              "tag-B" {:answered 1, :total 1}
              "tag-C" {:answered 2, :total 1}}
            (SUT/get-statistic-by-tags :fake-api "A"))))

    (testing "for two tags"
      (is (= {"tag-A" {:answered 0, :total 2}
              "tag-B" {:answered 2, :total 2}
              "tag-C" {:answered 4, :total 2}}
            (SUT/get-statistic-by-tags :fake-api ["A" "B"]))))

    (testing "for three tags"
      (is (= {"tag-A" {:answered 0, :total 3}
              "tag-B" {:answered 3, :total 3}
              "tag-C" {:answered 6, :total 3}}
            (SUT/get-statistic-by-tags :fake-api ["A" "B" "C"])))))

  (testing "Returns `nil` when we use unknown API"
    (is (nil? (SUT/get-statistic-by-tags :some-unknown-API ["something"])))

    (testing "or when the API server does not respond"
      (is (nil?
            (let [_ (.stop *api-server*)
                  response (SUT/get-statistic-by-tags :fake-api "A")
                  _ (.start *api-server*)]
              response)))))

  (testing "Returns error with message when we don't use tag in query"
    (is (true? (:error? (SUT/get-statistic-by-tags :fake-api nil))))
    (is (true? (:error? (SUT/get-statistic-by-tags :fake-api []))))))
