package org.akkamon.core.instruments

import org.akkamon.core.ActorStack

trait CounterTrait extends ActorStack {

   override def receive : Receive = {
     case x =>
       exporter.processCounter(s"count.invocation-${self.path.name}")
       super.receive(x)
   }
 }