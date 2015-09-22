A simple app that demonstrates Clojurescript and Figwheel.

### To run this you will need:

* A working copy of [Leiningen](https://github.com/technomancy/leiningen#installation)
* A working copy of [Sass](http://sass-lang.com/install)
* A recent version of [Java](http://www.oracle.com/technetwork/java/javase/downloads/index-jsp-138363.html)
* This project cloned

### When you have that:

Open a command line in the `todoer` directory and run:

```
lein figwheel
```

You should see something like this:

```
Figwheel: Starting server at http://localhost:3449
Focusing on build ids: dev
Compiling "resources/public/js/compiled/todoer.js" from ["src"]...
Successfully compiled "resources/public/js/compiled/todoer.js" in 1.042 seconds.
Started Figwheel autobuilder

Launching ClojureScript REPL for build: dev
Figwheel Controls:
          (stop-autobuild)                ;; stops Figwheel autobuilder
          (start-autobuild [id ...])      ;; starts autobuilder focused on optional ids
          (switch-to-build id ...)        ;; switches autobuilder to different build
          (reset-autobuild)               ;; stops, cleans, and starts autobuilder
          (reload-config)                 ;; reloads build config and resets autobuild
          (build-once [id ...])           ;; builds source one time
          (clean-builds [id ..])          ;; deletes compiled cljs target files
          (fig-status)                    ;; displays current state of system
          (add-dep [org.om/om "0.8.1"]) ;; add a dependency. very experimental
  Switch REPL build focus:
          :cljs/quit                      ;; allows you to switch REPL to another build
    Docs: (doc function-name-here)
    Exit: Control+C or :cljs/quit
 Results: Stored in vars *1, *2, *3, *e holds last exception object
Prompt will show when figwheel connects to your application
To quit, type: :cljs/quit
```

When that's done, visit [localhost:3449](http://localhost:3449/) and you should see the basic todo app. Updating files in the [clojurescript source directory](https://github.com/nhusher/todoer/blob/master/src/cljs/todoer/core.cljs) should immediately propagate changes to connected browser clients.

If you want to build the css, the `css.sh` shell script will do that. The `watch.sh` shell script will watch for changes and rebuild when it detects them. It can take several seconds for CSS changes to propagate to connected browsers -- not sure why.