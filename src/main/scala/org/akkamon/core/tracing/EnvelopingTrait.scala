package org.akkamon.core.tracing

import org.akkamon.core.ActorStack
import org.akkamon.core.extension.TraceExtension

/**
 * Simple trait that checks whether an incoming message contains an envelop. If so
 * it will use the configured trace exporter to send a trace message. Besides that
 * this trait provides a convience method to create a new envelop. Note that this must
 * be called explicitely before sending a message.
 */
trait EnvelopingTrait extends ActorStack {

  pipelineOuter(
    inner => {
      case env: Envelope =>
        // we've got a message that is already wrapped in an envelop. We can send some logging now to
        // the central trace store, whatever it might be.
        TraceExtension(context.system).storeTrace(env, sender(), self)
        receive(env);
      case x =>
        // we haven't got an envelope yet
        receive(x)
    })

}
