(ns config)

(defmacro getenv [key] (System/getenv key))
(defmacro appboard-url [] (or (getenv "APPBOARD_URL") "http://localhost:3000"))
(defmacro appbuilder-url [] (or (getenv "APPBUILDER_URL") "http://localhost:3004"))
(defmacro timeline-src [] (or (getenv "TIMELINE_SRC") "//cdn.knightlab.com/libs/timeline/latest/embed/index.html?source=1_OogiCWCFjzW1iSvRpifWbdWkERFyEAhQKWwNjVw-lQ&font=Bevan-PotanoSans&maptype=toner&lang=en&height=650"))
(defmacro app-calc-feature []
  (= "true" (clojure.string/lower-case (str (getenv "APP_CALC_FEATURE")))))
