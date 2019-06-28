package com.smort

import scala.concurrent.ExecutionContextExecutor
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

object ServiceMain extends App {
  // this configs are in the application.conf file
  val config = ConfigFactory.load()
  val host = config.getString("http.host") // Gets the host and a port from the configuration
  val port = config.getInt("http.port")

  implicit val system: ActorSystem = ActorSystem()  // ActorMaterializer requires an implicit ActorSystem
  implicit val ec: ExecutionContextExecutor = system.dispatcher  // bindingFuture.map requires an implicit ExecutionContext


  implicit val materializer: ActorMaterializer = ActorMaterializer()  // bindAndHandle requires an implicit materializer
}