logLevel := Level.Warn

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.0")
addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.5.1")