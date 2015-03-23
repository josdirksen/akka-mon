package org.akkamon.core.tracing

object Envelope {
  def apply[T](msg: T) = new Envelope(msg, Map[Property, Any](MessageTimestamp -> System.currentTimeMillis()))
  def apply[T](msg: T, properties: Map[Property, Any]) = new Envelope(msg, properties)
}

case class Envelope[T](msg: T,properties: Map[Property, Any])

trait Property
case object MessageTimestamp extends Property
case object SequenceId extends Property
case object CorrelationId extends Property
