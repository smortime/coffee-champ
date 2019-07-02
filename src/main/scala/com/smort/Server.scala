package com.smort

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.stream.ActorMaterializer
import com.smort.routes.RecommendationRoute
import com.typesafe.scalalogging.LazyLogging

import scala.io.StdIn
import scala.util.{ Failure, Success }

object Server extends App with LazyLogging {

  implicit val system = ActorSystem("coffee-chap")
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  // TODO: Move to config file
  val host = "127.0.0.1"
  val port = 8080

  val heartbeat: Route = get {
    complete("Coffee Champ API is running")
  }

  val recommendationRoutes = new RecommendationRoute().route()

  val routes: Route = recommendationRoutes ~ heartbeat

  val httpServerFuture = Http().bindAndHandle(routes, host, port)
  httpServerFuture.onComplete {
    case Success(binding) =>
      logger.info(s"Akka server is up and bound to ${binding.localAddress}")
    case Failure(e) =>
      logger.error(s"Akka failed to start", e)
      system.terminate()
  }

  StdIn.readLine()
  httpServerFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())

}
