name := "gatling-puppet-pcp"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.11.8"

enablePlugins(GatlingPlugin)
libraryDependencies += "io.gatling" % "gatling-test-framework" % "2.2.3" % "test"
libraryDependencies += "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.2.3" % "test"
