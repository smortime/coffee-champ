package com.smort.actors

import akka.actor.{ Actor, ActorLogging }
import akka.pattern._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Recommendation extends Actor with ActorLogging {
  import com.smort.messages.RecommendationSystemProtocol._

  def receive = {
    case Generate(name) =>
      log.info(s"Preference: $name")
      getRecommendation(name).pipeTo(sender)
  }

  def getRecommendation(preferences: Array[String]): Future[String] = Future {
    "Ethiopian"
  }
}
