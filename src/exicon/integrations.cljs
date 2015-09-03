(ns exicon.integrations
  (:require-macros
    [hoplon.core :refer [with-init!]]))

; ========== HubSpot ===========

(defn create-hubspot-form [custom-opts]
  (let [default-opts {:css ""}
        effective-opts (merge default-opts custom-opts)]
    (js/window.hbspt.forms.create (clj->js effective-opts))))

(defn hubspot-forms
  "Load hubspot form API.
  Create actual hubspot forms using the `create-hubspot-form` function
  from within the success callback."
  [success]
  (with-init!
    (.getScript js/jQuery "//js.hsforms.net/forms/v2.js"
                (comp success #(print "HubSpot forms integration loaded")))))

; ========== Inspectlet ===========

(defn inspect [field details]
  (js/__insp.push (array field details)))
