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
    var preference = ""
    if (preferences.contains("nutty") || preferences.contains("medium")) preference = "Colombia"
    else if (preferences.contains("chocolate") || preferences.contains("blueberry")) preference = "Ethiopia"
    else if (preferences.contains("fruity") || preferences.contains("caramel")) preference = "Brazil"
    else if (preferences.contains("zesty") || preferences.contains("spicy") || preferences.contains("earthy"))
      preference = "Panama"
    else if (preferences.contains("creamy") || preferences.contains("vanilla")) preference = "Uganda"
    else preference = "Guatemala"
    preference
  }
}
