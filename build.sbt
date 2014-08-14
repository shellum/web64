name := "web64"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "commons-codec" % "commons-codec" % "1.6",
  "redis.clients" % "jedis" % "2.5.2"
)     

play.Project.playScalaSettings
