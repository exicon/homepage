(ns exicon.integrations-via-getscript
  (:require-macros
    [tailrecursion.hoplon :refer [with-init! with-timeout defelem]])
  (:require
    [tailrecursion.hoplon :refer [script prerendering? timeout]]
    [goog.object :as o]))

(print "Using jQuery.getScript")

(defn default-global
  "Set a global variable to a default unless it already exists"
  [var-name default]
  (o/set js/window var-name (or (o/get js/window var-name) default)))

; (defn xhr-script-success [data, textStatus, jqxhr]
;   (js*
;     "console.log( data ); // Data returned
;     console.log( textStatus ); // Success
;     console.log( jqxhr.status ); // 200
;     console.log( 'Load was performed.' );"))

; ========== HubSpot ===========

(defn create-hubspot-form [custom-opts]
  (let [default-opts {:css ""}
        effective-opts (merge default-opts custom-opts)]
    (js/window.hbspt.forms.create (clj->js effective-opts))))

(defn hubspot-forms
  "Load hubspot form API.
  Create actual hubspot forms using the `create-hubspot-form` function
  from with an :onload handler."
  [success]
  (with-init!
    (.getScript js/jQuery "//js.hsforms.net/forms/v2.js"
                (comp success #(print "HubSpot forms integration loaded")))))

; ========== Inspectlet ===========

(defn inspect [field details]
  (.push js/__insp (clj->js [field details])))

(defn inspectlet [wid]
  #_(js* "window.__insp = window.__insp || []")

  (default-global "__insp" (array))
  (inspect "wid" wid)
  (with-init!
    (.getScript js/jQuery "//cdn.inspectlet.com/inspectlet.js"
                #(print "Inspectlet integration loaded"))))

; ========== Google Analytics ===========

(defn google-analytics []
  (with-init!
    #_(js* "(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
           (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
           m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
           })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

           ga('create', 'UA-31491340-1', 'auto');
           ga('require', 'displayfeatures');
           ga('send', 'pageview');
           ga('require', 'displayfeatures');")

    (set! js/window.GoogleAnalyticsObject "ga")
    (set! js/window.ga (fn [& args] (.push js/ga.q (clj->js args))))
    (set! js/ga.q (array))
    (set! js/ga.l (.getTime (js/Date.)))
    (js/ga "create" "UA-31491340-1" "auto")
    (js/ga "require" "displayfeatures")
    (js/ga "send" "pageview")
    (js/ga "require" "displayfeatures")
    (.getScript js/jQuery "//www.google-analytics.com/analytics.js"
                #(print "Google Analytics integration loaded"))))

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
