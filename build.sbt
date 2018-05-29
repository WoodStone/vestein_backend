name := """vestein-no-backend"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers += Resolver.sonatypeRepo("snapshots")
resolvers += "SQLite-JDBC Repository" at "https://oss.sonatype.org/content/repositories/snapshots"

scalaVersion := "2.12.4"

crossScalaVersions := Seq("2.11.12", "2.12.4")

libraryDependencies += jdbc
libraryDependencies += evolutions
libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
//libraryDependencies += "com.h2database" % "h2" % "1.4.196"
libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.20.1"
