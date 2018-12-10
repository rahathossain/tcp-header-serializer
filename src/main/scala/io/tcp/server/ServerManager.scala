package io.tcp.server

import scala.concurrent.duration._
import akka.actor.SupervisorStrategy.Restart
import akka.actor.{Actor, OneForOneStrategy, Props, ReceiveTimeout}

//# start ServerManager - companion object
object ServerManager {
  def props = Props(classOf[ServerManager])
  case object StartServer
}
//# end ServerManager - companion object


//# - start ServerManager - Actor
class ServerManager extends Actor {
  import ServerManager.StartServer

  self ! StartServer

  //context.setReceiveTimeout(60.seconds)
  override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 10 minute) {
    case _: Exception => Restart
  }

  override def receive: Receive = {
    case StartServer => context.watch( context.actorOf(Server.props) )
    case anything => println("-->>"+ anything)
    //case ReceiveTimeout => context.children foreach context.stop
  }
}
//# - end ServerManager - Actor