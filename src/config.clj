(ns config
  (:require
    [clojure.edn :as edn]))

(defmacro getenv [key] (System/getenv key))
(defmacro appboard-url [] (or (getenv "APPBOARD_URL") "http://localhost:3000"))
(defmacro appbuilder-url [] (or (getenv "APPBUILDER_URL") "http://localhost:3104"))
(defmacro timeline-src []
  (or (getenv "TIMELINE_SRC")
      "//cdn.knightlab.com/libs/timeline/latest/embed/index.html?source=1_OogiCWCFjzW1iSvRpifWbdWkERFyEAhQKWwNjVw-lQ&font=Bevan-PotanoSans&maptype=toner&lang=en&height=650"))
(defmacro inspectlet-feature []
  (= "true" (clojure.string/lower-case (str (getenv "INSPECTLET_FEATURE")))))
(defmacro inspectlet-wid [] (getenv "INSPECTLET_WID"))
(defmacro hs-portal-id [] (getenv "HS_PORTAL_ID"))
(defmacro hs-newsletter-id [] (getenv "HS_NEWSLETTER_ID"))
(defmacro hs-app-calc-id [] (getenv "HS_APP_CALC_ID"))
(defmacro hs-contact-us-id [] (getenv "HS_CONTACT_US_ID"))

(defmacro config
  [& keys]
  (let [env-cfg-file (str "./config.edn")
        config-opts (edn/read-string (slurp env-cfg-file))]
    (get-in config-opts keys)))
