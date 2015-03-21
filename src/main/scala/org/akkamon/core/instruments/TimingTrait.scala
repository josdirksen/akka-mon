package org.akkamon.core.instruments

import org.akkamon.core.ActorStack

trait TimingTrait extends ActorStack {

  override def receive: Receive = {
    case x =>
      val start = System.nanoTime();
      super.receive(x)
      exporter.processTimer(s"time.execution-${self.path.name}", System.nanoTime() - start);
  }
}
