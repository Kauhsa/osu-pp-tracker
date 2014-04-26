name := "osu! pp tracker"

version := "1.0"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.0",
  "net.databinder.dispatch" %% "dispatch-json4s-native" % "0.11.0",
  "org.json4s" %% "json4s-native" % "3.2.9"
)
