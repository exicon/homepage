(ns homepage.app-builder.rpc
  (:require-macros
    [tailrecursion.javelin :refer [defc defc=]]
    [config :as compile-time])
  (:require
   [tailrecursion.javelin :refer [cell]]
   [tailrecursion.castra :refer [mkremote]]))

(defc state {:random nil})
(defc submission-state :undefined)
(defc submission-error nil)
(defc error nil)
(defc loading [])
(defc config nil)
(defc= random-number (get state :random))

(defn remote [fn-name result & [error* loading*]]
  (mkremote (symbol (str "public-app-builder.api/" fn-name))
            result
            (or error* error)
            (or loading* loading)
            {:url (compile-time/config :backend-url)}))


(def save-submission
  (remote 'save-submission submission-state submission-error))

#_(def public-appbuilder-email
  (remote 'public-appbuilder-email state))

