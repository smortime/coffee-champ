organization in ThisBuild := "com.smort"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.8"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4" % Test

lazy val `coffee-champ` = (project in file("."))
  .aggregate(`coffee-champ-api`, `coffee-champ-impl`, `coffee-champ-stream-api`, `coffee-champ-stream-impl`)

lazy val `coffee-champ-api` = (project in file("coffee-champ-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `coffee-champ-impl` = (project in file("coffee-champ-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings)
  .dependsOn(`coffee-champ-api`)

lazy val `coffee-champ-stream-api` = (project in file("coffee-champ-stream-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `coffee-champ-stream-impl` = (project in file("coffee-champ-stream-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .dependsOn(`coffee-champ-stream-api`, `coffee-champ-api`)
