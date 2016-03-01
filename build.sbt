import scalariform.formatter.preferences._

name := "GrafanaToSlack"

version := "0.1"
sbtVersion := "0.13.9"
scalaVersion := "2.11.7"

lazy val `grafanatoslack` = (project in file("."))
  .enablePlugins(PlayScala)
  .enablePlugins(DockerPlugin)

routesGenerator := InjectedRoutesGenerator
routesImport += "java.util.UUID"

scalariformPreferences := scalariformPreferences.value
  .setPreference(DoubleIndentClassDeclaration, true)
  .setPreference(RewriteArrowSymbols, true)
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(IndentPackageBlocks, true)

packageName in Docker := packageName.value
version in Docker := version.value
dockerBaseImage := "java:8"
dockerExposedPorts := Seq(9000)
dockerRepository := Some("elufimov")
maintainer in Docker := "Michael Elufimov <elufimov@gmail.com>"

libraryDependencies ++= Seq(
  ws,
  "com.github.pathikrit"  %% "better-files"  % "2.14.0",
  "com.typesafe.play" %% "play-slick" % "1.1.1",
  "com.typesafe.play" %% "play-slick-evolutions" % "1.1.1",
  "org.postgresql" % "postgresql" % "9.4.1208"
)

