package io.rahat.tcp.server

import java.net.InetSocketAddress

import akka.actor.{Actor, Props}
import akka.io.{IO, Tcp}

//# Start Server - companion object
object Server {
  def props = Props(classOf[Server])
}
//# End Server - companion object


//# Start Server - Actor
class Server extends Actor {
  import Tcp._
  import context.system
  import Config._

  IO(Tcp) ! Bind(self, new InetSocketAddress(TCP_HOST, TCP_PORT))

  def receive = {
    case b @ Bound(localAddress) => context.parent ! b

    case CommandFailed(_: Bind) => context stop self

    case c @ Connected(remote, local) =>
      val handler = context.actorOf(Props[ServerHelper])
      val connection = sender()
      connection ! Register(handler)
  }

}
//# End Server - Actor