package com.smort.helpers

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

//TODO: Look into other means of json encoding...

final case class Recommendation(recommendation: String, preferences: Array[String])
final case class Preferences(items: Array[String])

trait JsonHelper extends SprayJsonSupport with DefaultJsonProtocol {
  import spray.json._
  implicit val printer = PrettyPrinter

  implicit val recommendationFormat = jsonFormat2(Recommendation)
  implicit val preferencesFormat = jsonFormat1(Preferences)
}