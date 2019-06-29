package com.smort.messages

object CoffeeChamp {
  case class CreateRecommendation(id: String, items: Vector[String])
  case class GetRecommendation(id: String)

  case class Recommendation(recommend: String, preferences: Vector[String])

  sealed trait RecommendationResponse
  case class RecommendationCreated(recommendation: Recommendation) extends RecommendationResponse
  case object RecommendationExists extends RecommendationResponse
}