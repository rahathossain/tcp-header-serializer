package io.tcp.server

import akka.actor.ActorSystem

object ServerApp extends App {

  println("Starting TCP Server...")
  val system = ActorSystem("serverApp")
  val serverManager = system.actorOf(ServerManager.props)

}