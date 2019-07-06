package io.rahat.tcp.client

import akka.actor.{Actor, Props}
import akka.io.Tcp.Connected
import akka.util.ByteString


//# Start ClientHelper - Companion object
object ClientHelper {
  def props = Props(classOf[ClientHelper])
}
//# Start ClientHelper - Companion object

//scala.io.StdIn.readLine()

//# Start ClientHelper - Actor
class ClientHelper extends Actor {

  override def receive: Receive = {

    case Connected(r, l) =>
      val messsage = "Hello from client"
      println(" --> Connected ( r = " +r+",  l = "+l )
      sender() ! ByteString(messsage)
    case msg: ByteString =>
      println(" ~~~> " + msg.utf8String)
    case _ =>
      println(" ==> " + _ )

  }

}
//# End ClientHelper - Actor
