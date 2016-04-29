package org.akkamon.core

import akka.actor.{ActorRef, ActorSystem, Props}
import org.akkamon.core.instruments.VMStatsInstrument

trait InstrumentExporter {

  // actor system to use for the stats collecting and sending it to the exporter
  implicit val system = ActorSystem("ExporterSystem")
  // to be defined by classes using this trait
  val instrumentActor: ActorRef
  // a special actor which collects system metrics
  val vmStatsActor = system.actorOf(Props(classOf[VMStatsInstrument.VMStatsActor], 1000l, instrumentActor))
  // keep track of the counters since start of system
  val counters = scala.collection.mutable.Map[String, Long]().withDefaultValue(0)

  // default implementations of the process message events. This sends an unformatted
  // message to the specified instrumentActor
  def processMessage(message: String) = instrumentActor ! MessageEvent(message)

  def processTimer(timer: String, value: Float) = instrumentActor ! TimerEvent(timer, value)

  def processCounter(key: String) = {
    counters.update(key, counters(key) + 1)
    instrumentActor ! CounterEvent(key, counters(key))
    instrumentActor ! SampledEvent(s"sampled-$key", 1)
  }

  def processCounterMap(counts: Map[String, Long]) = instrumentActor ! CounterEventMap(counts)

  // set of case classes used by this trait
  case class MessageEvent(message: String)

  case class TimerEvent(timer: String, value: Float)

  case class CounterEvent(key: String, value: Long)

  case class SampledEvent(key: String, value: Long)

  case class CounterEventMap(counts: Map[String, Long])

}
