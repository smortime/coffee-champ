package com.smort.coffeechamp.api

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}
import play.api.libs.json.{Format, Json}

object CoffeeChampService  {
  val TOPIC_NAME = "greetings"
}

/**
  * The Coffee Champ service interface.
  * <p>
  * This describes everything that Lagom needs to know about how to serve and
  * consume the CoffeeChampService.
  */
trait CoffeeChampService extends Service {


  def submitReview(name: String): ServiceCall[CoffeeReview, Done]

  override final def descriptor: Descriptor = {
    import Service._
    // @formatter:off
    named("coffee-champ")
      .withCalls(
        restCall(Method.POST, "/api/review/:name/:review/:rating", submitReview _)
      )
      .withAutoAcl(true)
    // @formatter:on
  }
}

case class CoffeeReview(name: String, review: String, rating: String)

object CoffeeReview {
  implicit val format: Format[CoffeeReview] = Json.format[CoffeeReview]
}
