package com.smort.coffeechamp.impl

class CoffeeChampServiceSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll {

  private val server = ServiceTest.startServer(
    ServiceTest.defaultSetup
      .withCassandra()
  ) { ctx =>
    new CoffeeChampApplication(ctx) with LocalServiceLocator
  }

  val client: CoffeeChampService = server.serviceClient.implement[CoffeeChampService]

  override protected def afterAll(): Unit = server.stop()

  "Coffee Champ service" should {

    "say hello" in {
      client.hello("Alice").invoke().map { answer =>
        answer should ===("Hello, Alice!")
      }
    }

    "allow responding with a custom message" in {
      for {
        _ <- client.useGreeting("Bob").invoke(GreetingMessage("Hi"))
        answer <- client.hello("Bob").invoke()
      } yield {
        answer should ===("Hi, Bob!")
      }
    }
  }
}
