package org.akkamon.core.instruments

import org.akkamon.core.ActorStack

trait LoggingTrait extends ActorStack {

  override def receive : Receive = {
    case x =>
      exporter.processMessage(s"Message received in ${self.path}")
      super.receive(x)
      exporter.processMessage(s"Message processed in ${self.path}")
  }
}