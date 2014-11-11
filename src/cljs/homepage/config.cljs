(ns homepage.config)

(defn env [] :development)
(defn env? [param] (= param (env)))
