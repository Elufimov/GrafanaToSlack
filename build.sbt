import scalariform.formatter.preferences._

name := "GrafanaToSlack"

version := "0.1"
sbtVersion := "0.13.9"
scalaVersion := "2.11.7"

lazy val `grafanatoslack` = (project in file(".")).enablePlugins(PlayScala)

routesGenerator := InjectedRoutesGenerator
routesImport += "java.util.UUID"

scalariformPreferences := scalariformPreferences.value
  .setPreference(DoubleIndentClassDeclaration, true)
  .setPreference(RewriteArrowSymbols, true)
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(IndentPackageBlocks, true)

libraryDependencies ++= Seq(
  ws,
  "com.github.pathikrit"  %% "better-files"  % "2.14.0",
  "net.gpedro.integrations.slack" % "slack-webhook" % "1.1.1",
  "com.typesafe.play" %% "play-slick" % "1.1.1",
  "com.typesafe.play" %% "play-slick-evolutions" % "1.1.1",
  "com.h2database" % "h2" % "1.4.191"
)

