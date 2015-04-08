package org.akkamon.core

import akka.actor.Actor
import akka.contrib.pattern.ReceivePipeline
import scala.reflect.runtime.universe

trait ActorStack extends Actor with ReceivePipeline {

  var actorName = self.path.name;

  // use the reflect module to get a reference to our object instance
  private val runtimeMirror = universe.runtimeMirror(getClass.getClassLoader)
  private val module = runtimeMirror.staticModule(Config.InstrumentExporter)
  val exporter = runtimeMirror.reflectModule(module).instance.asInstanceOf[InstrumentExporter]



}
