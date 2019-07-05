package com.smort

import akka.actor.ActorSystem
import akka.pattern._
import akka.testkit.{ DefaultTimeout, ImplicitSender, TestActorRef, TestKit }
import scala.concurrent.duration._
import scala.concurrent.Await
import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }
import com.smort.actors.Recommendation
import com.smort.messages.RecommendationSystemProtocol.Generate

import scala.util.Success

class RecommendationActorTest
    extends TestKit(ActorSystem("Recommendation"))
    with ImplicitSender
    with WordSpecLike
    with DefaultTimeout
    with BeforeAndAfterAll
    with Matchers {

  val actorRef = TestActorRef(new Recommendation)

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "A recommendation actor" must {
    "send back a single message" in {
      val future = actorRef ? Generate(Array("dark"))
      Await.result(future, 3 seconds)
      val Success(result: String) = future.value.get
      result should be("Ethiopian")
    }
  }
}
