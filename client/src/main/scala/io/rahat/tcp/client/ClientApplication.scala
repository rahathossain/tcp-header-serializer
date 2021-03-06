package io.rahat.tcp.client


import akka.actor.ActorSystem

object ClientApplication extends App {
  import ClientManager._

  println("Starting TCP Clients")
  val system = ActorSystem("tcpClientApp")

  val clientManager = system.actorOf(ClientManager.props)
  clientManager ! WakeUp
}
