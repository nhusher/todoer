(use 'figwheel-sidecar.repl-api)
(start-figwheel! {:all-builds (figwheel-sidecar.repl/get-project-cljs-builds)
                  :figwheel-options {:css-dirs ["resources/public/css"]
                                     :server-port 3448 }})
(cljs-repl)
