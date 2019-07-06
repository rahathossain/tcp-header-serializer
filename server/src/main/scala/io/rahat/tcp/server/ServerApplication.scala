package io.rahat.tcp.server

import akka.actor.ActorSystem

object ServerApplication extends App {

  println("Starting TCP Server...")
  val system = ActorSystem("serverApp")
  val serverManager = system.actorOf(ServerManager.props)

}