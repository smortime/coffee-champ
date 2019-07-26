package com.smort.routes

import akka.actor.{ ActorRef, ActorSystem, Props }
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern._
import akka.util.Timeout
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import com.smort.actors
import com.smort.helpers.{ JsonHelper, Preferences, Recommendation }
import com.smort.messages.RecommendationSystemProtocol.Generate
import com.typesafe.scalalogging.LazyLogging
import io.swagger.annotations._

import scala.concurrent.{ ExecutionContext, ExecutionContextExecutor, Future }

class RestApi(system: ActorSystem, timeout: Timeout) extends RecommendationRoute {
  implicit val requestTimeout: Timeout = timeout
  implicit def executionContext: ExecutionContextExecutor = system.dispatcher

  def createRecommendation(): ActorRef = system.actorOf(Props[actors.Recommendation], name = "RecommendationActor")
}

@Api(value = "/create-recommendation", description = "Operations for generating recommendations")
trait RecommendationRoute extends RecommendationApi with JsonHelper with LazyLogging {

  @ApiOperation(
    httpMethod = "POST",
    response = classOf[Recommendation],
    value = "Return a recommendation based off preferences")
  @ApiImplicitParams(
    Array(
      new ApiImplicitParam(
        name = "items",
        required = true,
        dataType = "array",
        paramType = "body",
        value = "List of preferences from user.")))
  def route(): Route = cors() {
    path("create-recommendation") {
      post {
        entity(as[Preferences]) { preferences =>
          logger.info(s"Recieved object: $preferences")

          val recommendationResponse = generateRecommendation(preferences.items)
          onSuccess(recommendationResponse) {
            case rec =>
              complete(StatusCodes.Created, Recommendation(rec, preferences.items))
          }
        }
      } ~ get {
        complete(StatusCodes.MethodNotAllowed, "GET is not supported for create-recommendation endpoint")
      }
    }
  }
}

trait RecommendationApi {

  def createRecommendation(): ActorRef

  implicit def executionContext: ExecutionContext
  implicit def requestTimeout: Timeout

  lazy val recommendation: ActorRef = createRecommendation()

  def generateRecommendation(items: Array[String]): Future[String] = {
    (recommendation ? Generate(items)).mapTo[String]
  }

}
