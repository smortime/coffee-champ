package com.smort.routes

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.unmarshalling.Unmarshaller.identityUnmarshaller
import com.smort.helpers.{JsonHelper, Recommendation, Preferences}
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

class RecommendationRoute extends JsonHelper with LazyLogging {

  def route(): Route = {
    path("create-recommendation") {
      post {
        entity(as[Preferences]) { preferences =>
          logger.info(s"Recieved object: $preferences")
          complete(StatusCodes.Created, Recommendation("Guatemalan", preferences.items))
        }
      } ~ get {
        complete(StatusCodes.MethodNotAllowed, "GET is not supported for create-recommendation endpont")
      }
    }
  }
}