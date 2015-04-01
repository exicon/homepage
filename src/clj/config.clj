(ns config)

(defmacro getenv [key] (System/getenv key))
(defmacro appboard-url [] (or (getenv "APPBOARD_URL") "http://localhost:3000"))
(defmacro appbuilder-url [] (or (getenv "APPBUILDER_URL") "http://localhost:3004"))
