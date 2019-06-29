package com.smort.actors

import akka.actor.{Actor, PoisonPill}
import com.smort.messages.CoffeeChamp

class Referrer(id: String) extends Actor {
  import com.smort.messages.Referrer._

  var preferences = Vector.empty[String]

  def receive: PartialFunction[Any, Unit] = {
    case SetPreferences(newPreferences) => preferences = newPreferences
    case GetRecommendation => sender() ! Some(("Guatemala", preferences))
    case Cancel => sender() ! Some(("No Recommendation", preferences))
      self ! PoisonPill
  }
}