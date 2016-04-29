package org.akkamon.core.instruments

import akka.contrib.pattern.ReceivePipeline
import akka.contrib.pattern.ReceivePipeline.Inner
import org.akkamon.core.ActorStack

trait LoggingTrait extends ActorStack {

  this: ReceivePipeline =>

  pipelineOuter {
    case x =>
      exporter.processMessage(s"Message received in ${actorName}")
      Inner(x).andAfter {
        exporter.processMessage(s"Message processed in ${actorName}")
      }
  }
}