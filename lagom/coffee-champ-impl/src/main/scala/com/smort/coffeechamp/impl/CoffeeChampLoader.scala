package com.smort.coffeechamp.impl

class CoffeeChampLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new CoffeeChampApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new CoffeeChampApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[CoffeeChampService])
}

abstract class CoffeeChampApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with CassandraPersistenceComponents
    with LagomKafkaComponents
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer: LagomServer = serverFor[CoffeeChampService](wire[CoffeeChampServiceImpl])

  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry: JsonSerializerRegistry = CoffeeChampSerializerRegistry

  // Register the Coffee Champ persistent entity
  persistentEntityRegistry.register(wire[CoffeeChampEntity])
}
