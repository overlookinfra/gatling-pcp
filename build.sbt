seq(clojure.settings :_*)

name := "gatling-puppet-pcp"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.11.8"

resolvers += "clojars" at "https://clojars.org/repo"

libraryDependencies += "org.clojure" % "clojure" % "1.6.0"
libraryDependencies += "puppetlabs" % "pcp-client" % "0.3.4"

enablePlugins(GatlingPlugin)
libraryDependencies += "io.gatling" % "gatling-test-framework" % "2.2.3" % "test"
libraryDependencies += "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.2.3" % "test"

javaOptions in Gatling := overrideDefaultJavaOptions("-Xms1024m", "-Xmx2048m")