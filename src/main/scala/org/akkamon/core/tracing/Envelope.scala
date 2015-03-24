package org.akkamon.core.tracing

import java.util.UUID

trait Envelope {
  val traceId: String = UUID.randomUUID().toString
}