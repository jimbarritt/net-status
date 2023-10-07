(ns net-status.core
  (:require [clj-http.client :as client]
            [clojure.java.io :as io]
            [clojure.core.async :as async]))

(defn log-response
  [log-writer url message]
  (.write log-writer (str (java.util.Date.) " - " url " - " message "\n")))

(defn head
  "Makes a HEAD call to a URL and returns the results"
  [url log-writer]
  (try 
    (let [response (client/head url {:throw-exceptions false
                                     :socket-timeout 900
                                     :connection-timeout 900
                                     :retry-handler (fn [ex try-count http-context] false)})]
      (log-response log-writer url (:status response)))
    (catch Throwable e
      (log-response log-writer url "NO-NETWORK"))
    ))


(defn poll-and-wait
  [url pause-ms log-file]
  (with-open [log-writer (io/writer log-file :append true)]
    (head url log-writer)  
    (Thread/sleep pause-ms)
    ))

(defn -main
  "Sets up a polling every 1 second to a url to check the internet status"
  []
  (println "Goung to start polling every second now CTRL-C to stop, logs in ~/net-status.log")
  (while true
    (poll-and-wait "https://www.google.com" 1000 "/Users/jmdb/net-status.log"))  
 )

