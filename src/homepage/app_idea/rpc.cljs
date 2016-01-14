(ns homepage.app-idea.rpc
  (:require-macros
    [javelin.core :refer [defc defc=]]
    [config :as compile-time])
  (:require
    [javelin.core :refer [cell]]
    [tailrecursion.castra :refer [mkremote]]
    [clojure.string :refer [includes?]]))

(defc state {:random nil})
(defc submission-state :undefined)
(defc submission-error nil)
(defc error nil)
(defc loading [])
(defc config nil)

(def log-response-for
  ["save-submission"])

(defn- assoc-when
  [m k v]
  (if-not v m (assoc m k v)))

(defn- xhr-resp-headers
  [xhr headers]
  (reduce #(if-let [x (.getResponseHeader xhr %2)]
            (assoc %1 %2 x) %1) {} headers))

(defn abortable-ajax-fn
  "Ajax request implementation using the standard jQuery ajax machinery."
  [{:keys [url timeout credentials headers body]}]
  (let [prom (.Deferred js/jQuery)
        opts (-> {"async"       true
                  "contentType" "application/json"
                  "data"        body
                  "dataType"    "text"
                  "headers"     headers
                  "processData" false
                  "type"        "POST"
                  "url"         url
                  "timeout"     timeout}
                 (assoc-when "xhrFields" (assoc-when nil "withCredentials" credentials)))
        resp (fn [x]
               {:status      (.-status x)
                :status-text (.-statusText x)
                :body        (.-responseText x)
                :headers     (xhr-resp-headers x ["X-Castra-Tunnel" "X-Castra-Session"])})
        xhr (.ajax js/jQuery (clj->js opts))]
    (-> xhr
        (.done (fn [_ _ x] (let [r (resp x)]
                             (when (some (partial includes? (get opts "data"))
                                         log-response-for)
                               (js/console.info
                                 (str "Castra response for"
                                      (get opts "data") "\n"
                                      (:body r))))
                             (.resolve prom r))))
        (.fail (fn [x _ _] (.reject prom (resp x)))))
    (js/console.info "Castra request: %s" (get opts "data"))
    (doto prom (aset "xhr" xhr))))

(defn remote [fn-name result & [error* loading*]]
  (mkremote (symbol (str "public-app-builder.api/" fn-name))
            result
            (or error* error)
            (or loading* loading)
            {:url     (compile-time/config :backend-url)
             :ajax-fn abortable-ajax-fn}))


(def save-submission
  (remote 'save-submission submission-state submission-error))

#_(def public-appbuilder-email
  (remote 'public-appbuilder-email state))

