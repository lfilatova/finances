(defproject finances "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.7.1"

  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/clojurescript "1.10.773"]
                 [rum "0.12.3"]
                 [tick "0.4.32"]
                 [cljsjs/highcharts "8.2.2-0"]]

  :source-paths ["src"]

  :aliases {"fig"       ["trampoline" "run" "-m" "figwheel.main"]
            "fig:dev"   ["trampoline" "run" "-m" "figwheel.main" "-b" "finances" "-r"]
            "fig:test"  ["run" "-m" "figwheel.main" "-co" "test.cljs.edn" "-m" "finances.test-runner"]
            "fig:build" ["run" "-m" "figwheel.main" "-O" "advanced" "-bo" "finances"]}

  :profiles {:dev {:dependencies   [[com.bhauman/figwheel-main "0.2.12"]
                                    [com.bhauman/rebel-readline-cljs "0.1.4"]]

                   :resource-paths ["target"]
                   :clean-targets  ^{:protect false} ["target" "resources/public/cljs-out"]}})

