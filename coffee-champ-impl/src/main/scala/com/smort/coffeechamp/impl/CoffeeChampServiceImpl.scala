package com.smort.coffeechamp.impl

import com.smort.coffeechamp.api
import com.smort.coffeechamp.api.CoffeeChampService
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.broker.TopicProducer
import com.lightbend.lagom.scaladsl.persistence.{EventStreamElement, PersistentEntityRegistry}

/**
  * Implementation of the CoffeeChampService.
  */
class CoffeeChampServiceImpl(persistentEntityRegistry: PersistentEntityRegistry) extends CoffeeChampService {

  override def hello(id: String) = ServiceCall { _ =>
    // Look up the Coffee Champ entity for the given ID.
    val ref = persistentEntityRegistry.refFor[CoffeeChampEntity](id)

    // Ask the entity the Hello command.
    ref.ask(Hello(id))
  }

  override def useGreeting(id: String) = ServiceCall { request =>
    // Look up the Coffee Champ entity for the given ID.
    val ref = persistentEntityRegistry.refFor[CoffeeChampEntity](id)

    // Tell the entity to use the greeting message specified.
    ref.ask(UseGreetingMessage(request.message))
  }


  override def greetingsTopic(): Topic[api.GreetingMessageChanged] =
    TopicProducer.singleStreamWithOffset {
      fromOffset =>
        persistentEntityRegistry.eventStream(CoffeeChampEvent.Tag, fromOffset)
          .map(ev => (convertEvent(ev), ev.offset))
    }

  private def convertEvent(helloEvent: EventStreamElement[CoffeeChampEvent]): api.GreetingMessageChanged = {
    helloEvent.event match {
      case GreetingMessageChanged(msg) => api.GreetingMessageChanged(helloEvent.entityId, msg)
    }
  }
}
