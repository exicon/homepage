(ns config)

(defmacro getenv [key] (System/getenv key))
(defmacro env? [e] (= (getenv "ENV") (name e)))
(defmacro appboard-url [] (or (getenv "APPBOARD_URL") "http://localhost:3000"))
