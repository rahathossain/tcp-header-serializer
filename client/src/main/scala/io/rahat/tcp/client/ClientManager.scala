package io.rahat.tcp.client

import java.net.InetSocketAddress

import akka.actor.SupervisorStrategy.Restart
import akka.actor.{Actor, OneForOneStrategy, Props}
import akka.io.Tcp.Connected
import akka.util.ByteString


import scala.concurrent.duration._


//# Start ClientManager - Companion object
object ClientManager {
  def props = Props(classOf[ClientManager])
  case object WakeUp
}
//# Start ClientManager - Companion object

//scala.io.StdIn.readLine()

//# Start ClientManager - Actor
class ClientManager extends Actor {
  import ClientManager._
  import Config._
  import MessageExtractor._

  //context.setReceiveTimeout(60.seconds)
  override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 10 minute) {
    case _: Exception => Restart
  }


  override def receive: Receive = {
    case WakeUp =>
      val remote = new InetSocketAddress(TCP_HOST, TCP_PORT)
      context.watch(
        context.actorOf(Client.props(remote, self)) )

    case Connected(r, l) =>
      val messsage =
        """.ecnereffid eht lla edam sah taht dnA
          |,yb delevart ssel eno eht koot I
          |dna ,doow a ni degrevid sdaor owT
          |:ecneh sega dna sega erehwemoS
          |hgis a htiw siht gnillet eb llahs I
          |
          |.kcab emoc reve dluohs I fi detbuod I
          |,yaw ot no sdael yaw woh gniwonk teY
          |!yad rehtona rof tsrif eht tpek I ,hO
          |.kcalb neddort dah pets on sevael nI
          |yal yllauqe gninrom taht htob dnA
          |
          |,emas eht tuoba yllaer meht nrow daH
          |ereht gnissap eht taht rof sa hguohT
          |;raew detnaw dna yssarg saw ti esuaceB
          |,mialc retteb eht spahrep gnivah dnA
          |,riaf sa tsuj sa ,rehto eht koot nehT
          |
          |;htworgrednu eht ni tneb ti erehw oT
          |dluoc I sa raf sa eno nwod dekool dnA
          |doots I gnol ,relevart eno eb dnA
          |htob levart ton dluoc I yrros dnA
          |,doow wolley a ni degrevid sdaor owT
          |=========================================
          |TSORF TREBOR YB
          |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
          |nekaT toN daoR ehT
          |
        """.stripMargin


      println(" --> Connected ( r = " +r+",  l = "+l )

      sender() ! encode(ByteString("akahD."))
      sender() ! encode(ByteString("yendyS."))
      sender() ! encode(ByteString(messsage))


    case msg: ByteString =>
      context.actorOf(MessageExtractor.props(sender())) ! Extract(msg)

    case _ =>
      println(" ==> " + _ )

    //case ReceiveTimeout => context.children foreach context.stop

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
  //def decode(byteString: ByteString) : ByteString =

}
//# End ClientManager - Actor





