(ns config)

(defmacro getenv [key] (System/getenv key))
(defmacro env? [e] (= (getenv "ENV") (name e)))
