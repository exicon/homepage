(ns config)

(defmacro getenv [key] (System/getenv key))
(defmacro appboard-url [] (or (getenv "APPBOARD_URL") "http://localhost:3000"))
