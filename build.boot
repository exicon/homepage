(set-env!
  :project 'homepage
  :version "0.1.0-SNAPSHOT"
  :dependencies
  '[[tailrecursion/castra "3.0.0-SNAPSHOT"]
    [tailrecursion/boot-hoplon "0.1.3"]
    [tailrecursion/hoplon "6.0.0-alpha6"]
    ; [tailrecursion/javelin "3.8.0"]
    ; [cljsjs/jquery "2.1.4-0"]
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
  '[cljsjs.boot-cljsjs :refer [from-cljsjs]]
  '[clojure.java.io :as io])

(task-options!
  speak { :theme "woodblock" })

(deftask copy-index-htmls
  [d dirs DIRS #{str} "Directories for the main index.html to be accessible under"]
  (with-pre-wrap
    fileset
    (let [index-html* (->> fileset ls
                           (by-re [#"^index.html$"
                                   #"^index.html.js$"
                                   #"^index.html.out"]))
          in-subdir (fn [f subdir]
                      (let [subdir-f (str subdir (.path f))]
                        {subdir-f (assoc f :path subdir-f)}))
          dirs-with-index (into {}
                                (for [dir dirs
                                      f index-html*]
                                  (in-subdir f dir)))
          fileset' (update-in fileset [:tree] merge dirs-with-index)]
      (commit! fileset'))))

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
    ; (reload)
    (cljs :optimizations :none :source-map true)
    (copy-index-htmls
      :dirs #{"app-builder/"
              "app-builder-submission/"
              "app-calculator/"
              "appboard/"
              "customers/"
              "developers/"
              "news/"
              "newsletters/"
              "presentations/"
              "privacy-policy/"
              "reports/"
              "terms-of-use/"
              "videos/"})
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
