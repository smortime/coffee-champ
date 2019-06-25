package com.smort.coffeechampstream.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import com.smort.coffeechampstream.api.CoffeeChampStreamService
import com.smort.coffeechamp.api.CoffeeChampService
import com.softwaremill.macwire._

class CoffeeChampStreamLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new CoffeeChampStreamApplication(context) {
      override def serviceLocator: NoServiceLocator.type = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new CoffeeChampStreamApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[CoffeeChampStreamService])
}

abstract class CoffeeChampStreamApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer: LagomServer = serverFor[CoffeeChampStreamService](wire[CoffeeChampStreamServiceImpl])

  // Bind the CoffeeChampService client
  lazy val coffeeChampService: CoffeeChampService = serviceClient.implement[CoffeeChampService]
}
