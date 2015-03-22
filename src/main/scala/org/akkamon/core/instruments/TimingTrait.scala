package org.akkamon.core.instruments

import akka.contrib.pattern.ReceivePipeline
import org.akkamon.core.ActorStack

trait TimingTrait extends ActorStack {

  this: ReceivePipeline =>

  pipelineOuter(
    inner => {
      case x =>
        val start = System.nanoTime();
        inner(x)
        exporter.processTimer(s"time.execution-${self.path.name}", System.nanoTime() - start);
    })
}
