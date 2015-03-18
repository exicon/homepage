(ns config
	(:require
		[clojure.edn :as edn]
		[tailrecursion.boot.core :refer [get-env]]))

(defmacro getenv [key] (System/getenv key))
(defmacro env? [e] (= (getenv "ENV") (name e)))
(defmacro appboard-url [] (or (getenv "APPBOARD_URL") "http://localhost:3000"))


(defn config
	([]
		(let [env-cfg-file (str "config/" (get-env :env) ".edn")]
			(edn/read-string (slurp env-cfg-file))))
	([& keys]
		(get-in (config) keys)))
