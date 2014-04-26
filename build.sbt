name := "osu! pp tracker"

version := "1.0"

scalaVersion := "2.10.4"

resolvers ++= Seq(
  "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases/"
)

libraryDependencies ++= Seq(
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.0",
  "net.databinder.dispatch" %% "dispatch-json4s-native" % "0.11.0",
  "org.json4s" %% "json4s-native" % "3.2.9",
  "net.ceedubs" %% "ficus" % "1.0.0"
)

