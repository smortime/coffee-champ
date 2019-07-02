package com.smort.coffeechamp.api

import akka.Done
import play.api.libs.json.{Format, Json}

object CoffeeChampService  {
  val TOPIC_NAME = "coffee-champ"
}

/**
  * The Coffee Champ service interface.
  * <p>
  * This describes everything that Lagom needs to know about how to serve and
  * consume the CoffeeChampService.
  */
trait CoffeeChampService extends Service {


  def setPreferences(id: String): ServiceCall[CoffeePreferences, Done]

  override final def descriptor: Descriptor = {
    // @formatter:off
    named("coffee-champ")
      .withCalls(
        restCall(Method.POST, "/api/preferences/:id", setPreferences _)
      )
      .withAutoAcl(true)
    // @formatter:on
  }
}

case class CoffeePreferences(preferences: List[String])

object CoffeePreferences {
  implicit val format: Format[CoffeePreferences] = Json.format
}
