package org.akkamon.core.instruments

import akka.contrib.pattern.ReceivePipeline
import akka.contrib.pattern.ReceivePipeline.Inner
import org.akkamon.core.ActorStack

trait TimingTrait extends ActorStack {

  this: ReceivePipeline =>

  pipelineOuter {
    case x =>
      val start = System.nanoTime()
      Inner(x).andAfter {
        exporter.processTimer(s"time.execution-${actorName}", System.nanoTime() - start)
      }
  }
}
