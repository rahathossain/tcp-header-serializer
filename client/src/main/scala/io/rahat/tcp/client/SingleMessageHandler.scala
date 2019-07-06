package io.rahat.tcp.client

import akka.actor.{Actor, ActorRef, Props}
import akka.util.ByteString


//# Start SingleMessageHandler - companion object
object SingleMessageHandler {
  def props(data: ByteString, client: ActorRef) = Props(new SingleMessageHandler(data, client))
  case object HandleSingle
  case object Sent
}
//# End SingleMessageHandler - companion object

//# Start SingleMessageHandler - Actor
class SingleMessageHandler(data: ByteString, client: ActorRef) extends Actor  {
  import SingleMessageHandler._

  context.self ! HandleSingle

  override def receive: Receive = {
    case HandleSingle =>
      println(data.utf8String)
      context stop self
  }

}
//# End SingleMessageHandler - Actor

