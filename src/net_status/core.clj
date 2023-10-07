(ns net-status.core
  (:require [clj-http.client :as client]
            [clojure.java.io :as io]
            [clojure.core.async :as async]))

(defn log-response
  [log-writer url response]
  (.write log-writer (str (java.util.Date.) " - " url " - " (:status response) "\n")))

(defn head
  "Makes a HEAD call to a URL and returns the results"
  [url log-file]
  (with-open [log-writer (io/writer log-file :append true)]
    (let [response (client/get url {:throw-exceptions false})]
      (log-response log-writer url response))
    ))



(defn -main
  "Sets up a polling every 1 second to a url to check the internet status"
  []
  (ping "https://www.google.com"))

