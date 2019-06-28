name := "coffee-champ"

version := "0.1"

scalaVersion := "2.12.8"

organization := "com.smort"

libraryDependencies ++= {
  val akkaVersion = "2.5.14"
  val akkaHttp = "10.1.8"
  Seq(
    "com.typesafe.akka" %% "akka-actor"      % akkaVersion,
    "com.typesafe.akka" %% "akka-http-core"  % akkaHttp,
    "com.typesafe.akka" %% "akka-http"       % akkaHttp,
    "com.typesafe.play" %% "play-ws-standalone-json"       % "1.1.8",
    "com.typesafe.akka" %% "akka-slf4j"      % akkaVersion,
    "ch.qos.logback"    %  "logback-classic" % "1.2.3",
    "de.heikoseeberger" %% "akka-http-play-json"   % "1.17.0",
    "com.typesafe.akka" %% "akka-testkit"    % akkaVersion   % "test",
    "org.scalatest"     %% "scalatest"       % "3.0.5"       % "test"
  )
}