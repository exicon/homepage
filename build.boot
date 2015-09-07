(set-env!
  :project 'homepage
  :version "0.1.0-SNAPSHOT"
  :dependencies
  '[[tailrecursion/castra "3.0.0-SNAPSHOT"]
    [hoplon/boot-hoplon "0.1.7"]
    [hoplon/hoplon "6.0.0-alpha9"]
    [adzerk/boot-reload "0.3.1"]
    [pandeiro/boot-http "0.6.3"]
    [org.clojure/clojurescript "1.7.48"]
    [adzerk/boot-cljs "1.7.48-3"]
    [cljsjs/boot-cljsjs "0.5.0" :scope "test"]
    [exicon/semantic-ui "2.0.6-SNAPSHOT"]]
  :source-paths #{"src"}
  :resource-paths #{"assets"})

(require
  '[hoplon.boot-hoplon :refer [hoplon prerender html2cljs]]
  '[adzerk.boot-reload :refer [reload]]
  '[pandeiro.boot-http :refer [serve]]
  '[adzerk.boot-cljs :refer [cljs]]
  '[cljsjs.boot-cljsjs :refer [from-cljsjs]]
  '[clojure.java.io :as io]
  '[clojure.pprint :refer :all]
  '[boot.util :refer [info]])

(task-options!
  speak {:theme "woodblock"}
  cljs {:compiler-options
        {:pseudo-names false}})

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
    (hoplon :pretty-print true)
    (reload)
    (cljs :optimizations :none
          :source-map true)
    (copy-index-htmls)
    ; (prerender)
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
    (cljs :optimizations :advanced :source-map true)
    (copy-index-htmls)
    (prerender)
    #_(sift :invert true :include #{#"index.html.out/"})))

(deftask stg [] (prod))
