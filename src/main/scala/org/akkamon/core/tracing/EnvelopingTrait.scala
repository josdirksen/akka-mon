package org.akkamon.core.tracing

import org.akkamon.core.ActorStack

trait EnvelopingTrait extends ActorStack {

  pipelineOuter(
    inner => {
      case env: Envelope =>
        // we've got a message that is already wrapped in an envelop. We can send some logging now to
        // the central trace store, whatever it might be.
        receive(env.msg);
      case x =>
        // we haven't got an envelope yet
        receive(x)
    })


  def addEnvelope(msg: Any): Envelope[Any] = {
    Envelope(msg)
  }
}
