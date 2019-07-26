package com.smort

import java.io.InputStream
import java.security.{ KeyStore, SecureRandom }

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives.{ complete, get }
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.{ ConnectionContext, Http, HttpsConnectionContext }
import akka.stream.ActorMaterializer
import akka.util.Timeout
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import com.smort.routes.RestApi
import com.typesafe.scalalogging.LazyLogging
import com.smort.swagger.SwaggerDocService
import javax.net.ssl.{ KeyManagerFactory, SSLContext, TrustManagerFactory }

import scala.concurrent.duration._
import scala.util.{ Failure, Success }

object Server extends App with LazyLogging {

  implicit val system = ActorSystem("coffee-champ")
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  // TODO: Move to config file
  val host = sys.env("HOST")
  val port = sys.env("PORT").toInt

  val heartbeat: Route = cors() {
    get {
      complete("Coffee Champ API is running")
    }
  }

  val recommendationRoutes = new RestApi(system, Timeout(30 seconds)).route()
  val swagger = SwaggerDocService.routes

  val routes: Route = recommendationRoutes ~ heartbeat ~ swagger

  // Mannual HTTPS setup
  val password: Array[Char] = sys.env.getOrElse("CERT_PASSWORD", "").toCharArray

  val ks: KeyStore = KeyStore.getInstance("PKCS12")
  val keystore: InputStream =
    getClass.getClassLoader.getResourceAsStream("keys/server.p12")

  require(keystore != null, "Keystore required!")
  ks.load(keystore, password)

  val keyManagerFactory: KeyManagerFactory = KeyManagerFactory.getInstance("SunX509")
  keyManagerFactory.init(ks, password)

  val tmf: TrustManagerFactory = TrustManagerFactory.getInstance("SunX509")
  tmf.init(ks)

  val sslContext: SSLContext = SSLContext.getInstance("TLS")
  sslContext.init(keyManagerFactory.getKeyManagers, tmf.getTrustManagers, new SecureRandom)
  val https: HttpsConnectionContext = ConnectionContext.https(sslContext)

  val httpServerFuture = Http().bindAndHandle(routes, host, port, connectionContext = https)
  httpServerFuture.onComplete {
    case Success(binding) =>
      logger.info(s"Akka server is up and bound to ${binding.localAddress}")
    case Failure(e) =>
      logger.error(s"Akka failed to start", e)
      system.terminate()
  }
}
