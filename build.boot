(set-env!
  :project 'homepage
  :version "0.1.0-SNAPSHOT"
  :dependencies
  '[[tailrecursion/castra "3.0.0-SNAPSHOT"]
    [hoplon/boot-hoplon "0.1.9"]
    [hoplon/hoplon "6.0.0-alpha10"]
    ; [adzerk/boot-reload "0.4.0"]
    [pandeiro/boot-http "0.6.3"]
    [org.clojure/clojure "1.7.0"]
    [org.clojure/clojurescript "1.7.228"]
    [adzerk/boot-cljs "1.7.228-1"]
    [adzerk/cljs-console "0.1.1"]
    [cljsjs/boot-cljsjs "0.5.0" :scope "test"]
    [exicon/semantic-ui "2.1.4-SNAPSHOT"]
    [camel-snake-kebab "0.3.2"]
    [formative "0.8.8"]]
  :source-paths #{"src"}
  :resource-paths #{"assets"})

(require
  '[hoplon.boot-hoplon :refer [hoplon prerender html2cljs]]
  ; '[adzerk.boot-reload :refer [reload]]
  '[pandeiro.boot-http :refer [serve]]
  '[adzerk.boot-cljs :refer [cljs]]
  '[cljsjs.boot-cljsjs :refer [from-cljsjs]]
  '[clojure.java.io :as io]
  '[clojure.pprint :refer :all]
  '[boot.util :refer [info]])

(task-options!
  ; reload {:open-file "echo %s -- %s -- %s -- subl %3$s:%1$s"}
  cljs {:compiler-options
        {:pseudo-names false}}
  pom {:project 'homepage
       :version "0.1.0"})

(deftask copy-index-html
  [d dirs DIRS #{str} "Directories for the main index.html to be accessible under"]
  (with-pre-wrap
    fileset
    (let [_ (info "Copying index.html...\n")
          index-html* (->> fileset ls
                           (by-re [#"^index.html$"
                                   #"^index.html.js$"
                                   #"^index.html.out"]))
          copy-into (fn [subdir fs file]
                      (let [file-in-subdir (str subdir (tmp-path file))]
                        (cp fs (tmp-file file)
                            (assoc file :path file-in-subdir))))
          copy-index-html-into (fn [fs subdir]
                                 (info "• %s\n" subdir)
                                 (reduce (partial copy-into subdir)
                                         fs index-html*))]
      (commit! (reduce copy-index-html-into fileset dirs)))))

(deftask copy-index-htmls []
  (copy-index-html
    :dirs #{"about-us/"
            "appbuilder/"
            "app-idea/"
            "appboard/"
            "pricing/"
            "customers/"
            "developers/"
            "news/"
            "newsletters/"
            "presentations/"
            "privacy-policy/"
            "reports/"
            "terms-of-use/"
            ; "videos/"
            }))

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
    (speak)
    (hoplon :pretty-print true)
    ; (reload)   ; Causes the `with-init` functions called infinitely
    (cljs :optimizations :none
          :source-map true)
    (copy-index-htmls)))

(deftask prod
  "Build homepage for production."
  []
  (comp
    (from-cljsjs :profile :production)
    (sift :to-resource #{#"themes"})
    (sift :to-resource #{#"semantic-ui.min.inc.css"})
    (sift :move {#"^semantic-ui.min.inc.css$" "semantic-ui.css"})
    (hoplon)
    (cljs :optimizations :advanced :source-map true)
    (copy-index-htmls)
    (prerender)
    (target)
    #_(sift :invert true :include #{#"index.html.out/"})))

(deftask stg [] (prod))
