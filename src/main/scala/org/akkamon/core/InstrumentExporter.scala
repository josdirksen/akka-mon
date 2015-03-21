package org.akkamon.core

import akka.actor.{ActorRef, Actor, ActorSystem}

trait InstrumentExporter {

  import scala.collection.mutable.Map

  implicit val system = ActorSystem("ExporterSystem")
  val counters = Map[String,Integer]().withDefaultValue(0)
  val instrumentActor: ActorRef

  def processMessage(message: String) = instrumentActor ! MessageEvent(message)

  def processTimer(timer: String, value: Float) = instrumentActor ! TimerEvent(timer, value)

  def processCounter(key: String) = {
    counters.update(key, counters(key) + 1)
    instrumentActor ! CounterEvent(key, counters(key))
  }

  case class MessageEvent(message: String);
  case class TimerEvent(timer: String, value: Float);
  case class CounterEvent(key: String, value: Integer);
}
