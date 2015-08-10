#!/usr/bin/env boot

#tailrecursion.boot.core/version "2.5.1"

(set-env!
	:project      'homepage
	:version      "0.1.0-SNAPSHOT"
	:dependencies '[
		[tailrecursion/boot.task    "2.2.4"]
		[tailrecursion/hoplon       "5.10.24"]
		[com.novemberain/validateur "2.4.2"]
		[exicon/hoplon5-semantic-ui "2.0.6-SNAPSHOT"]]
	:out-path     "resources/public"
	:src-paths    #{"src/hl" "src/cljs" "src/clj" "assets/javascript"})

;; Static resources (css, images, etc.):
(add-sync! (get-env :out-path) #{"assets"})

(require '[tailrecursion.hoplon.boot :refer :all]
	'[tailrecursion.castra.task :as c])

(def server (c/castra-dev-server 'homepage.api :port 3103))

(deftask development
	"Build homepage for development."
	[]
	(comp (watch) (hoplon {:prerender false}) server))

(deftask dev-debug
	"Build homepage for development with source maps."
	[]
	(comp (watch) (hoplon {:pretty-print true
		:prerender false
		:source-map true}) server))

(deftask production
	"Build homepage for production."
	[]
	(hoplon {
		:pretty-print true
		:prerender true
		:optimizations :simple}))

(deftask staging
	"Build homepage for staging."
	[]
	(production))
