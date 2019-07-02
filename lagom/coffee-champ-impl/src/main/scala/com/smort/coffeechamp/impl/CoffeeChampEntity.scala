package com.smort.coffeechamp.impl

import java.time.Instant

import akka.Done

import scala.collection.immutable.Seq

class CoffeeChampEntity extends PersistentEntity {

  override type Command = CoffeeChampCommand[_]
  override type Event = CoffeeChampEvent
  override type State = CoffeeChampState

  /**
   * The initial state. This is used if there is no snapshotted state to be found.
   */
  override def initialState: CoffeeChampState = CoffeeChampState(List.empty)

  /**
   * An entity can define different behaviours for different states, so the behaviour
   * is a function of the current state to a set of actions.
   */
  override def behavior: Behavior = {
    Actions().onCommand[SetPreferences, Done] {
      case (SetPreferences(preferences), ctx, _) =>
        ctx.thenPersist(PreferencesSet(preferences, Instant.now())) { _ =>
          ctx.reply(Done)
        }
    }
  }

  def eventHandlers: EventHandler = {
    case (PreferencesSet(preferences: List[String], _), state) => state.setPreferences(preferences)
  }

}

/**
 * The current state held by the persistent entity.
 */
case class CoffeeChampState(sPreferences: List[String]) {
  def setPreferences(preferences: List[String]) = {
    copy(sPreferences = preferences ++ sPreferences)
  }
}

object CoffeeChampState {

  /**
   * Format for the hello state.
   *
   * Persisted entities get snapshotted every configured number of events. This
   * means the state gets stored to the database, so that when the entity gets
   * loaded, you don't need to replay all the events, just the ones since the
   * snapshot. Hence, a JSON format needs to be declared so that it can be
   * serialized and deserialized when storing to and from the database.
   */
  implicit val format: Format[CoffeeChampState] = Json.format
}

/**
 * This interface defines all the events that the CoffeeChampEntity supports.
 */
sealed trait CoffeeChampEvent extends AggregateEvent[CoffeeChampEvent] {
  def aggregateTag: AggregateEventTag[CoffeeChampEvent] = CoffeeChampEvent.Tag
}

object CoffeeChampEvent {
  val Tag: AggregateEventTag[CoffeeChampEvent] = AggregateEventTag[CoffeeChampEvent]
}

case class PreferencesSet(preferences: List[String], evenTime: Instant) extends CoffeeChampEvent

object PreferencesSet {
  implicit val format: Format[PreferencesSet] = Json.format
}

/**
 * This interface defines all the commands that the CoffeeChampEntity supports.
 */
sealed trait CoffeeChampCommand[R] extends ReplyType[R]
case class SetPreferences(preferences: List[String]) extends CoffeeChampCommand[Done]

object SetPreferences {
  implicit val format: Format[SetPreferences] = Json.format
}

case class CoffeeChampException(message: String) extends RuntimeException(message)

object CoffeeChampException {
  implicit val format: Format[CoffeeChampException] = Json.format[CoffeeChampException]
}

/**
 * Akka serialization, used by both persistence and remoting, needs to have
 * serializers registered for every type serialized or deserialized. While it's
 * possible to use any serializer you want for Akka messages, out of the box
 * Lagom provides support for JSON, via this registry abstraction.
 *
 * The serializers are registered here, and then provided to Lagom in the
 * application loader.
 */
object CoffeeChampSerializerRegistry extends JsonSerializerRegistry {
  override def serializers: Seq[JsonSerializer[_]] =
    Seq(JsonSerializer[CoffeeChampState], JsonSerializer[PreferencesSet], JsonSerializer[SetPreferences])
}
