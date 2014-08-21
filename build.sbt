name := "web64"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "commons-codec" % "commons-codec" % "1.6",
  "redis.clients" % "jedis" % "2.5.2",
  "com.google.inject" % "guice" % "3.0",
  "javax.inject" % "javax.inject" % "1",
  "org.mockito" % "mockito-core" % "1.9.5" % "test"
)     

play.Project.playScalaSettings
