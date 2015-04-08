package org.akkamon.core.instruments

import akka.contrib.pattern.ReceivePipeline
import org.akkamon.core.ActorStack

trait CounterTrait extends ActorStack {

  this: ReceivePipeline ⇒

  pipelineOuter(
    inner => {
      case x =>
        exporter.processCounter(s"count.invocation-${actorName}")
        inner(x)
    })
 }