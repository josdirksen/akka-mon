package org.akkamon.core.instruments

import akka.contrib.pattern.ReceivePipeline
import akka.contrib.pattern.ReceivePipeline.Inner
import org.akkamon.core.ActorStack

trait CounterTrait extends ActorStack {

  this: ReceivePipeline =>


  pipelineOuter {
    case x =>
      exporter.processCounter(s"count.invocation-${actorName}")
      Inner(x)
  }
}