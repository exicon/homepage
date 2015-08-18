(set-env!
  :project 'homepage
  :version "0.1.0-SNAPSHOT"
  :dependencies
  '[[exicon/boot-hoplon "0.1.2-SNAPSHOT"]
    [tailrecursion/hoplon "6.0.0-alpha2"]
    [adzerk/boot-reload "0.3.1"]
    [pandeiro/boot-http "0.6.3"]
    [adzerk/boot-cljs "1.7.48-SNAPSHOT"]
    [cljsjs/boot-cljsjs "0.5.0" :scope "test"]
    [exicon/semantic-ui "2.0.6-SNAPSHOT"]]
  :source-paths #{"src"}
  :resource-paths #{"assets"})

(require
  '[tailrecursion.boot-hoplon :refer [hoplon prerender html2cljs]]
  '[adzerk.boot-reload :refer [reload]]
  '[pandeiro.boot-http :refer [serve]]
  '[adzerk.boot-cljs :refer [cljs]]
  '[cljsjs.boot-cljsjs :refer [from-cljsjs]])

(task-options!
  speak { :theme "woodblock" })

(deftask dev
  "Build homepage for development."
  []
  (comp
    (serve :port 3103)
    (from-cljsjs :profile :development)
    (sift :to-resource #{#"themes"})
    (sift :to-resource #{#"semantic-ui.inc.css"})
    (sift :move {#"^semantic-ui.inc.css$" "semantic-ui.css"})
    (watch)
    (hoplon :pretty-print true)
    (reload)
    (cljs :optimizations :none
          :source-map false)
    (speak)))

(deftask prod
  "Build homepage for production."
  []
  (comp
    (from-cljsjs :profile :production)
    (sift :to-resource #{#"themes"})
    (sift :to-resource #{#"semantic-ui.min.inc.css"})
    (sift :move {#"^semantic-ui.min.inc.css$" "semantic-ui.css"})
    (hoplon)
    (cljs :optimizations :simple)
    (sift :invert true :include #{#"^out/"})
    (prerender)))

(deftask stg [] (prod))
