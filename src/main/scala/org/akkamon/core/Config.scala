package org.akkamon.core

object Config {

  //  val InstrumentExporter: String  = "org.akkamon.core.exporters.ConsoleExporter"
  val InstrumentExporter = "org.akkamon.core.exporters.StatsdExporter"

  val StatsdHost = "localhost"
  val StatsdPort = 8125
}
