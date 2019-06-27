package com.smort.coffeechamp.impl

import akka.Done
import com.smort.coffeechamp.api
import com.smort.coffeechamp.api.{CoffeeChampService, CoffeeReview}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.{EventStreamElement, PersistentEntityRegistry}

/**
  * Implementation of the CoffeeChampService.
  */
class CoffeeChampServiceImpl(persistentEntityRegistry: PersistentEntityRegistry) extends CoffeeChampService {

  def submitReview(name: String): ServiceCall[CoffeeReview, Done] = ServiceCall { request =>
    val ref = persistentEntityRegistry.refFor[CoffeeChampEntity](name)

    ref.ask(SubmitReviewCommand(request.name, request.rating, request.review))
  }
}
