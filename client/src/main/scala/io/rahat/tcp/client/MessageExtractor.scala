package io.rahat.tcp.client

import akka.actor.{Actor, ActorRef, Props}
import akka.util.ByteString

//# Start MessageExtractor - companion object
object MessageExtractor {
  def props(client: ActorRef) = Props(new MessageExtractor(client))
  case class Extract(data: ByteString)
}
//# End MessageExtractor - companion object

//# Start MessageExtractor - Actor
class MessageExtractor(client: ActorRef) extends Actor {
  import MessageExtractor._
  import SingleMessageHandler._
  import Config._

  override def receive: Receive = {

    case Extract(data) =>
      val (first, more) = extract(data)
      context.actorOf(SingleMessageHandler.props(first, client))
      if(!more.isEmpty) self ! Extract(more)

    case Sent =>
      if(context.children.size == 0 ) context stop self

  }


  def extract(data: ByteString) = {

    val head : Int =
      headerLengthInByte match {
        case ONE_BYTE => data.head
        case TWO_BYTES => (data.head << 8) + (data.tail.head & 0xFF)
        case FOUR_BYTES => (data.head << 24) + (data.tail.head << 16) +
                              (data.tail.tail.head << 8) + (data.tail.tail.tail.head & 0xFF)
        case _ => throw new IllegalArgumentException("Header Length must be 1, 2 or 4")
      }
    val dropHead = data.drop(headerLengthInByte)
    val payload = dropHead.take(head)
    def trailer = data.drop(headerLengthInByte).drop(head)

    ( payload, trailer )

  }

}
//# End MessageExtractor - Actor

