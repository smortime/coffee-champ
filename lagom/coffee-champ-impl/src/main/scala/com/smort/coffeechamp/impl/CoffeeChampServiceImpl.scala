package com.smort.coffeechamp.impl

import akka.Done

import scala.concurrent.ExecutionContext

/**
  * Implementation of the CoffeeChampService.
  */
class CoffeeChampServiceImpl(persistentEntityRegistry: PersistentEntityRegistry)(implicit ec: ExecutionContext) extends CoffeeChampService {

  private def entityRef(id: String) =
    persistentEntityRegistry.refFor[CoffeeChampEntity](id)

  override def setPreferences(id: String): ServiceCall[CoffeePreferences, Done] = ServiceCall { request =>
    entityRef(id)
      .ask(SetPreferences(request.preferences))
      .recover {
        case CoffeeChampException(message) => throw BadRequest(message)
      }
  }
}
