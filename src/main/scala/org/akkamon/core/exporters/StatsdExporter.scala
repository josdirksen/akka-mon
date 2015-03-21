package org.akkamon.core.exporters

import java.net.InetSocketAddress

import akka.actor.{ActorRef, Actor, Props}
import akka.io.{Udp, IO}
import akka.util.ByteString
import org.akkamon.core.{Config, InstrumentExporter}

/**
 * This exporters exports using UDP to statsD. Note that this exporter is started asynchronously, so if your
 * system starts processing messages directly, the first couple of messages might be missed.
 */
object StatsdExporter extends InstrumentExporter {

  def formatEvent(event: TimerEvent): String = s"${event.timer}:${event.value}|ms"
  def formatEvent(event: CounterEvent): String = s"${event.key}:${event.value}|g"

  val instrumentActor = system.actorOf(Props(classOf[SimpleSender], new InetSocketAddress(Config.StatsdHost, Config.StatsdPort)))

  class SimpleSender(remote: InetSocketAddress) extends Actor {

    IO(Udp) ! Udp.SimpleSender

    def receive = {
      case Udp.SimpleSenderReady =>
        context.become(ready(sender()))
    }

    def ready(send: ActorRef): Receive = {
      case msg: MessageEvent => // do nothing, a message can't be sent to statsd
      case timer: TimerEvent => send ! Udp.Send(ByteString(formatEvent(timer)), remote)
      case counter: CounterEvent => send ! Udp.Send(ByteString(formatEvent(counter)), remote)
    }
  }
}

