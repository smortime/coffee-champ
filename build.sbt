enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)
enablePlugins(AshScriptPlugin)

name := "coffee-champ"
version := "0.2"
scalaVersion := "2.12.8"
organization := "com.smort"

mainClass in Compile := Some("com.smort.Server")

libraryDependencies ++= {
  val akkaVersion = "2.5.19"
  val akkaHttp = "10.1.8"
  val sparkVersion = "2.4.3"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-http-core" % akkaHttp,
    "com.typesafe.akka" %% "akka-http" % akkaHttp,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttp,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
    "ch.megard" %% "akka-http-cors" % "0.4.1",
    "org.apache.spark" %% "spark-core" % sparkVersion,
    "org.apache.spark" %% "spark-mllib" % sparkVersion,
    "com.github.swagger-akka-http" %% "swagger-akka-http" % "1.1.0",
    "org.scalatest" %% "scalatest" % "3.0.5" % "test")
}

dockerBaseImage := "openjdk:jre-alpine"
