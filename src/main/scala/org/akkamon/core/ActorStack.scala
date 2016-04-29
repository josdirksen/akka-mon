package org.akkamon.core

import akka.actor.Actor
import akka.contrib.pattern.ReceivePipeline

import scala.reflect.runtime.universe

trait ActorStack extends Actor with ReceivePipeline {
  private val runtimeMirror = universe.runtimeMirror(getClass.getClassLoader)
  // use the reflect module to get a reference to our object instance
  private val module = runtimeMirror.staticModule(Config.InstrumentExporter)
  var actorName = self.path.name
  val exporter = runtimeMirror.reflectModule(module).instance.asInstanceOf[InstrumentExporter]


}
