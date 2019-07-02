package com.smort

import akka.actor.{ ActorSystem, Props }
import akka.pattern._
import akka.util.Timeout
import com.smort.actors._
import com.smort.messages.RecommendationSystemProtocol.Generate

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object MainTest extends App {
  val system = ActorSystem("coffee-champ")
  val recommendationActor = system.actorOf(Props[Recommendation], name = "RecommendationActor")

  import scala.concurrent.duration._
  implicit val timeout = Timeout(5 second)

  val recommendationResult: Future[String] = (recommendationActor ? Generate("earthy")).mapTo[String]

  for {
    response <- recommendationResult
  } yield println(s"You'd really like $response coffees")

  Thread.sleep(5000)

  val isTerminated = system.terminate()
}
