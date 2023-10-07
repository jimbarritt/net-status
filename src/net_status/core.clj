(ns net-status.core)

(defn ping
  "Makes a HEAD call to a URL and returns the results"
  [url]
  (println "Going to ping " url))

(defn -main
  "Sets up a polling every 1 second to a url to check the internet status"
  []
  (ping "https://www.google.com"))
