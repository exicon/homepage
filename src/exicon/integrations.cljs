(ns exicon.integrations
  (:require-macros
    [hoplon.core :refer [with-init! defelem]])
  (:require
    [hoplon.core :refer [script prerendering?]]
    [goog.object :as o]))

(defn append-head [elem] (-> (js/$ "head") (aget 0) (.appendChild elem)))

(defn- load-error [desc]
  (.error js/console (str "Can't load async script:" desc)))

(defn- async-script [{:keys [onload onerror src] :as opts}]
  (let [attrs (dissoc opts :onload :onerror)
        base-script (script :async true
                            :charset "utf-8"
                            :type "text/javascript")
        custom-script (base-script attrs)]
    (set! (.-onload custom-script) onload)
    (set! (.-onerror custom-script) (or onerror (load-error src)))
    (with-init! (append-head custom-script))))

; ========== HubSpot ===========

(defn create-hubspot-form [custom-opts]
  (let [default-opts {:css ""}
        effective-opts (merge default-opts custom-opts)]
    (.. js/hbspt -forms (create (clj->js effective-opts)))))

(defn hubspot-forms
  "Load hubspot form API.
  Create actual hubspot forms using the `create-hubspot-form` function
  from with an :onload handler."
  [& {:as opts}]
  (async-script
    (merge {:src "https://js.hsforms.net/forms/v2.js"
            :onerror #(load-error "HubSpot forms integration")}
           opts)))

; ========== Inspectlet ===========

(defn inspect [field details]
  (.push js/__insp (clj->js [field details])))

(defn inspectlet [wid]
  ; (js* "window.__insp = window.__insp || []")
  (o/set js/window "__insp"
         (or (o/get js/window "__insp")
             (array)))
  (inspect "wid" wid)
  (async-script {:id "inspsync" :src "//cdn.inspectlet.com/inspectlet.js"
                 :onerror #(load-error "Inspectlet integration")
                 :onload #(print "Inspectlet integration loaded")}))

; ========== Google Analytics ===========

(defn google-analytics []
  (with-init!
    (js* "(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
         (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
         m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
         })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

         ga('create', 'UA-31491340-1', 'auto');
         ga('require', 'displayfeatures');
         ga('send', 'pageview');
         ga('require', 'displayfeatures');")))

; ========== HubSpot Analytics ===========

(defn hubspot-analytics []
  (with-init!
    (js*
      "(function(d,s,i,r) {
      if (d.getElementById(i)){return;}
      var n=d.createElement(s),e=d.getElementsByTagName(s)[0];
      n.id=i;n.src='//js.hs-analytics.net/analytics/'+(Math.ceil(new Date()/r)*r)+'/511335.js';
      e.parentNode.insertBefore(n, e);
      })(document,\"script\",\"hs-analytics\",300000);")))
