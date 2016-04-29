package org.akkamon.core.extension

import akka.actor._
import org.akkamon.core.tracing.Envelope

object TraceExtension
  extends ExtensionId[TraceExtensionImpl]
    with ExtensionIdProvider {

  override def lookup = TraceExtension

  //This method will be called by Akka
  // to instantiate our Extension
  override def createExtension(system: ExtendedActorSystem) = new TraceExtensionImpl
}

class TraceExtensionImpl extends Extension {

  //This is the operation this Extension provides
  def storeTrace(env: Envelope, sender: ActorRef, receiver: ActorRef): Unit = {
    println(s"Msg received with tracer id: ${env.traceId} from ${sender.path.name} on ${receiver.path.name}")
  }
}