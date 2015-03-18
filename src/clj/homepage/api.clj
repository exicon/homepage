(ns homepage.api
	(:require
		[tailrecursion.castra :refer [defrpc]]
		[config :refer [config]]))

(defrpc get-state []
	{:random (rand-int 100)})

(defrpc get-config []
	(select-keys (config)
		[:appboard-url]))
