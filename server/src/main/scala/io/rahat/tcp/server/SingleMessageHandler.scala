package io.rahat.tcp.server

import akka.actor.{Actor, ActorRef, Props}
import akka.io.Tcp
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
  import Config._
  import SingleMessageHandler._
  import Tcp._

  context.self ! HandleSingle

  override def receive: Receive = {
    case HandleSingle =>
      client ! Write(encode(data.reverse))
      context.parent ! Sent
      context stop self
  }

  def encode(data: ByteString) : ByteString = {
    headerLengthInByte match {
      case ONE_BYTE => ByteString(data.length) ++ data
      case TWO_BYTES =>
        val x = data.length
        ByteString( x >> 8 ) ++ ByteString(( x << 24 ) >> 24) ++ data
      case FOUR_BYTES =>
        val x = data.length
        ByteString( x >> 24 ) ++ ByteString( (x & 0xFFFFFF)  >> 16 ) ++
          ByteString( ( ( x & 0xFFFF) >> 8 )  ) ++ ByteString( x & 0xFF ) ++ data
      case _ => throw new IllegalArgumentException("Invalid header Length In Byte, Header Length must be 1, 2 or 4")
    }
  }
}
//# End SingleMessageHandler - Actor