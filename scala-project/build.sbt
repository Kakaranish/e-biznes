name := "ebiznes"
 
version := "1.0" 
      
lazy val `ebiznes` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"
      
scalaVersion := "2.12.2"

// Init dependencies
libraryDependencies ++= Seq(
  ehcache,
  ws,
  specs2 % Test,
  guice
)

// Custom dependencies
libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "4.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "4.0.0",
  "org.xerial" % "sqlite-jdbc" % "3.7.2"
)
unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )
