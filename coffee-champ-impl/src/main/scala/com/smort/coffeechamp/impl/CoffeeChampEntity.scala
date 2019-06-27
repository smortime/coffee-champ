package com.smort.coffeechamp.impl

import java.time.LocalDateTime

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, PersistentEntity}
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import play.api.libs.json.{Format, Json}

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
    case CoffeeChampState(reviews) => Actions()
      .onCommand[SubmitReviewCommand, Done] {
      case (SubmitReviewCommand(name, review, rating), ctx, state) =>
        ctx.thenPersist(
          AddToReviewsEvent(review)
        ) { _ =>
          ctx.reply(Done)
        }
    }.onEvent {
      case (AddToReviewsEvent(review), state) =>
        CoffeeChampState(review :: state.reviews)
    }
  }
}


/**
  * The current state held by the persistent entity.
  */
case class CoffeeChampState(reviews: List[String])
case class AddToReviewsEvent(review: String) extends CoffeeChampEvent

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

/**
  * This interface defines all the commands that the CoffeeChampEntity supports.
  */
sealed trait CoffeeChampCommand[R] extends ReplyType[R]
final case class SubmitReviewCommand(name: String, review: String, rating: String) extends CoffeeChampCommand[Done]




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
  override def serializers: Seq[JsonSerializer[_]] = Seq(
    JsonSerializer[CoffeeChampState]
  )
}
