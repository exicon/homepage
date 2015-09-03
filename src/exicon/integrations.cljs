(ns exicon.integrations
  (:require-macros
    [hoplon.core :refer [with-init! defelem]])
  (:require
    [hoplon.core :refer [script]]))

(def ^:private hubspot-forms-to-load (atom []))

(defn load-hubspot-form [opts]
  (swap! hubspot-forms-to-load conj (merge {:css ""} opts)))

(defn hubspot-forms
  "Load hubspot form API, then create forms registered by `load-hubspot-form`."
  []
  (with-init!
    (js/jQuery.getScript
      "//js.hsforms.net/forms/v2.js"
      #(doseq [{:keys [target] :as opts} @hubspot-forms-to-load]
         (print "Loading HubSpot form" target)
         (js/hbspt.forms.create (clj->js opts))))))

(defelem hubspot-analytics []
  [(script
     "(function(d,s,i,r) {
     if (d.getElementById(i)){return;}
     var n=d.createElement(s),e=d.getElementsByTagName(s)[0];
     n.id=i;n.src='//js.hs-analytics.net/analytics/'+
     (Math.ceil(new Date()/r)*r)+'/511335.js';
     e.parentNode.insertBefore(n, e);
     })(document,'script','hs-analytics',300000);")
   (script "console.log('HubSpot analytics kicked off')")])

(defelem google-analytics []
  [(script
     "(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
     (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
     m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
     })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

     ga('create', 'UA-31491340-1', 'auto');
     ga('require', 'displayfeatures');
     ga('send', 'pageview');
     ga('require', 'displayfeatures');")
   (script "console.log('Google Analytics kicked off')")])

(defelem inspectlet []
  [(script
     :id "inspectletjs"
     "window.__insp = window.__insp || [];
     __insp.push(['wid', 1724490325]);
     (function() {
     function ldinsp(){var insp = document.createElement('script'); insp.type = 'text/javascript'; insp.async = true; insp.id = \"inspsync\"; insp.src = ('https:' == document.location.protocol ? 'https' : 'http') + '://cdn.inspectlet.com/inspectlet.js'; var x = document.getElementsByTagName('script')[0]; x.parentNode.insertBefore(insp, x); };
     document.readyState != \"complete\" ? (window.attachEvent ? window.attachEvent('onload', ldinsp) : window.addEventListener('load', ldinsp, false)) : ldinsp();
     })();")
   (script "console.log('Inspectlet kicked off')")])

(defn inspect [field details]
  (js/__insp.push (array field details)))
