package com.smort.actors

import akka.actor._
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import com.smort.messages.CoffeeChamp._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class CoffeeChamp(implicit timeout: Timeout) extends Actor {
  import com.smort.messages.Referrer

  def createReferrer(id: String): ActorRef = {
    context.actorOf(Referrer.props(id), id)
  }

  def receive: PartialFunction[Any, Unit] = {
    case CreateRecommendation(id, items) =>
      def create(): Unit = {
        val referrer = createReferrer(id)
        referrer ! Referrer.SetPreferences(items)
        sender() ! RecommendationCreated(Recommendation("Guatemala", items))
      }
      context.child(id).fold(create())(_ => sender() ! RecommendationExists)

    case GetRecommendation(id) =>
      def notFound(): Unit = sender() ! None
      def get(child: ActorRef): Unit = child.forward(Referrer.GetRecommendation)
      context.child(id).fold(notFound())(get)
  }
}