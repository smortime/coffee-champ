package com.smort.coffeechamp.impl

import akka.actor.ActorSystem

class CoffeeChampEntitySpec extends WordSpec with Matchers with BeforeAndAfterAll {

  private val system =
    ActorSystem("CoffeeChampEntitySpec", JsonSerializerRegistry.actorSystemSetupFor(CoffeeChampSerializerRegistry))

  override protected def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  private def withTestDriver(
      block: PersistentEntityTestDriver[CoffeeChampCommand[_], CoffeeChampEvent, CoffeeChampState] => Unit): Unit = {
    val driver = new PersistentEntityTestDriver(system, new CoffeeChampEntity, "coffee-champ-1")
    block(driver)
    driver.getAllIssues should have size 0
  }

  "Coffee Champ entity" should {

    "say hello by default" in withTestDriver { driver =>
      val outcome = driver.run(Hello("Alice"))
      outcome.replies should contain only "Hello, Alice!"
    }

    "allow updating the greeting message" in withTestDriver { driver =>
      val outcome1 = driver.run(UseGreetingMessage("Hi"))
      outcome1.events should contain only GreetingMessageChanged("Hi")
      val outcome2 = driver.run(Hello("Alice"))
      outcome2.replies should contain only "Hi, Alice!"
    }

  }
}
