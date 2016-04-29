package org.akkamon.core.exporters

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef, Props}
import akka.io.{IO, Udp}
import akka.util.ByteString
import org.akkamon.core.{Config, InstrumentExporter}

/**
  * This exporters exports using UDP to statsD. Note that this exporter is started asynchronously, so if your
  * system starts processing messages directly, the first couple of messages might be missed.
  */
object StatsdExporter extends InstrumentExporter {

  val instrumentActor = system.actorOf(Props(classOf[SimpleSender], new InetSocketAddress(Config.StatsdHost, Config.StatsdPort)))

  def formatEvent(event: TimerEvent): String = s"${event.timer}:${event.value}|ms"

  def formatEvent(event: CounterEvent): String = s"${event.key}:${event.value}|g"

  // we sent this as a gauge so statsd keeps track of the values
  def formatEvent(event: SampledEvent): String = s"${event.key}:${event.value}|c"

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
      case counter: SampledEvent => send ! Udp.Send(ByteString(formatEvent(counter)), remote)
      case counterMap: CounterEventMap =>
        counterMap.counts.foreach { case (key, value) => send ! Udp.Send(ByteString(formatEvent(CounterEvent(key, value))), remote) }
    }
  }

}

