(set-env!
	:dependencies '[
		[adzerk/boot-cljs-repl     "0.1.8"]
		[adzerk/boot-reload        "0.2.4"]
		[pandeiro/boot-http        "0.6.1"]
		[markdown-clj              "0.9.62"]
		[adzerk/boot-cljs          "0.0-2760-0"]
		[tailrecursion/boot-hoplon "0.1.0"]
		[tailrecursion/hoplon      "6.0.0-SNAPSHOT"]
		[cljsjs/boot-cljsjs "0.4.6" :scope "test"]
		[cljsjs/semantic-ui "1.9.1-0"]
		[com.novemberain/validateur "2.4.2"]]
	:resource-paths    #{"assets"}
	:target-path    "resources/public"
	:source-paths  #{"src/hl" "src/cljs" "src/clj"})

(require
	'[adzerk.boot-cljs :refer [cljs]]
	'[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
	'[adzerk.boot-reload :refer [reload]]
	'[pandeiro.boot-http :refer [serve]]
	'[tailrecursion.boot-hoplon :refer [hoplon prerender html2cljs]]
	'[cljsjs.boot-cljsjs :refer [from-cljsjs]])

(deftask dev
	"Build hoplon.io for local development."
	[]
	(comp
		(watch)
		(from-cljsjs :profile :development)
		(sift :to-resource #{#"themes"})
		(sift :to-resource #{#"semantic-ui.inc.css"})
		(hoplon :pretty-print true)
		(reload)
		(cljs :optimizations :none :source-map false)
		(serve :dir (get-env :target-path) :port 3003)
		(speak)))

(deftask prod
	"Build hoplon.io for production deployment."
	[]
	(comp
		(hoplon :pretty-print true)
		(cljs :optimizations :advanced :source-map true)
		(prerender)))
