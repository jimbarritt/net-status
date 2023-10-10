(ns net-status.core
  (:gen-class)
  (:require [clj-http.client :as client]
            [clojure.java.io :as io]
            [clojure.core.async :as async]
            [clojure.tools.cli :refer [parse-opts]]
            [me.raynes.fs :as fs]
            [clj-yaml.core :as yaml]))

(def cli-options
  ;; An option with a required argument
  [["-l" "--logfile FILENAME" "Log file path"
    :default "logs/net-status.log"]
   
   ])

(def status-store (atom {}))

(defn register-site
  "Adds a url as a site to the status atom"
  [site-key url]
  (swap! status-store assoc site-key {:url url :status :unknown}))

(defn update-site-status
  "Updates the status of a site"
  [site-key status]
  (swap! status-store assoc-in [site-key :status] status))

(defn get-site-status
  "Returns the current site status"
  [site-key]
  (get-in @status-store [site-key :status]))

(defn get-site-url
  "Returns the URL for the site"
  [site-key]
  (get-in @status-store [site-key :url]))


(defn log-message
  [log-file url message]
  (with-open [log-writer (io/writer log-file :append true)]
     (.write log-writer (str (java.util.Date.) " - " url " - " message "\n"))))
 


;; You can add {:debug true} to the /head call to see what its actually passing
(defn test-status
  "Makes a HEAD call to a URL and returns a status based on the result or if theres a network error"
  [url]
  (try 
    (let [response (client/head url {:throw-exceptions false
                                     :socket-timeout 900
                                     :connection-timeout 900
                                     :headers {:content-length 0} ;; Some serversd are a bit fussy about this e.g. Brother Printer
                                     :retry-handler (fn [ex try-count http-context] false)})]
      (if (= 200 (:status response))
        :site-up
        :site-up-bad-request)
      )
    (catch Throwable e
      :site-down)
    ))


(defn poll-site
  [site-key log-file]
  (let [url (get-site-url site-key)
        recorded-status (get-site-status site-key)
        actual-status (test-status url)]   

    (if (not (= actual-status recorded-status))
      (log-message log-file url (str "Status [" recorded-status "] to [" actual-status "]")))
    (update-site-status site-key actual-status)))

(defn process-config
  "Loads a config file and registers the sites"
  [config-file]
  (let [config (yaml/parse-string (slurp config-file))
        sites (:sites config)]
    (doseq [site sites]
      (let [{:keys [name url]} site]
      (println "Registering Site: " name " - " url)
      (register-site (keyword name) url)))))

(defn register-default-site
  "Registers the default to be google"
  []
  (register-site :google "https://www.google.com")
  (println "Using default google config"))

(defn configure-sites
  "Loads the sites to configure from a config file in ~/.config/net-status.config If theres no file, defaults to google"
  []
  (let [config-file (fs/expand-home "~/.config/net-status/net-status.conf")]
    (if (fs/exists? config-file)
      (process-config config-file)
      (register-default-site))))

(defn -main
  "Sets up a polling every 1 second to a url to check the internet status"
  [& args]
  (let [options (:options (parse-opts args cli-options))
        logfile (:logfile options)]
    (println "Going to start polling every second now CTRL-C to stop, logs in [" logfile "]")
    (.mkdirs (.getParentFile (io/file logfile)))
    (configure-sites)
    (while true
      (doseq [key (keys @status-store)]
              (poll-site key logfile))
;;      (poll-site :google logfile)
      (Thread/sleep 1000))))

