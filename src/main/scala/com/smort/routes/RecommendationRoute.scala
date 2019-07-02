package com.smort.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.smort.helpers.{ JsonHelper, Preferences, Recommendation }
import com.typesafe.scalalogging.LazyLogging

import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

class RecommendationRoute extends JsonHelper with LazyLogging {

  def route(): Route = cors() {
    path("create-recommendation") {
      post {
        entity(as[Preferences]) { preferences =>
          logger.info(s"Recieved object: $preferences")
          complete(StatusCodes.Created, Recommendation("Guatemalan", preferences.items))
        }
      } ~ get {
        complete(StatusCodes.MethodNotAllowed, "GET is not supported for create-recommendation endpoint")
      }
    }
  }
}
