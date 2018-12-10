package io.tcp.server

import akka.actor.{Actor, Props}
import akka.io.Tcp

//# Start SimplisticHandler - companion object
object ServerHelper {
  def props = Props(classOf[ServerHelper])
}
//# End SimplisticHandler - companion object


//# Start SimplisticHandler - Actor
class ServerHelper extends Actor {
  import MessageExtractor._
  import Tcp._

  def receive = {
    case Received(data) => context.actorOf(MessageExtractor.props(sender())) ! Extract(data)
    case PeerClosed     => context stop self
  }
}
//# End SimplisticHandler - Actor