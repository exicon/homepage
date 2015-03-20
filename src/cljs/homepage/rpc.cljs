(ns homepage.rpc
	(:require-macros
		[tailrecursion.javelin :refer [defc defc=]]
	)
	(:require
		[tailrecursion.javelin :refer [cell]]
		[tailrecursion.castra :refer [mkremote]]
	)
)

(defc state {:random nil})
(defc error nil)
(defc loading [])
(defc= random-number (get state :random))

(def get-state
	(mkremote 'homepage.api/get-state state error loading))

(defn init []
	#_(get-state)
	#_(js/setInterval get-state 1000))
