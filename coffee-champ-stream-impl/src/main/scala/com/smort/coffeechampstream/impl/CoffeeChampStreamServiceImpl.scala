package com.smort.coffeechampstream.impl

import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.smort.coffeechampstream.api.CoffeeChampStreamService
import com.smort.coffeechamp.api.CoffeeChampService

import scala.concurrent.Future

/**
  * Implementation of the CoffeeChampStreamService.
  */
class CoffeeChampStreamServiceImpl(coffeeChampService: CoffeeChampService) extends CoffeeChampStreamService {
  def stream = ServiceCall { hellos =>
    Future.successful(hellos.mapAsync(8)(coffeeChampService.hello(_).invoke()))
  }
}
