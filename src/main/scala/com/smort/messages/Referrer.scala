package com.smort.messages

import akka.actor.Props
import com.smort.actors.Referrer

object Referrer {

  def props(id: String) = Props(new Referrer(id))

  case class SetPreferences(preferences: Vector[String])

  case object GetRecommendation
  case object Cancel
}