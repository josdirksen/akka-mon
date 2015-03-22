package org.akkamon.core.instruments

import java.io.File
import akka.actor.{ActorRef, Actor}
import org.akkamon.core.ActorStack
import scala.concurrent.duration._

object VMStatsInstrument {

  class VMStatsActor(interval: Long, exporterActor: ActorRef) extends ActorStack {
    import context._

    override def preStart() =
      system.scheduler.scheduleOnce(2 * interval millis, self, "tick")

    // override postRestart so we don't call preStart and schedule a new message
    override def postRestart(reason: Throwable) = {}

    def receive = {
      case "tick" =>
        // send another periodic tick after the specified delay
        system.scheduler.scheduleOnce(interval millis, self, "tick")
        exporter.processCounterMap(getStats())
    }
  }

  def getStats(): Map[String, Long] = {

    val baseStats = Map[String, Long](
      "count.procs" -> Runtime.getRuntime().availableProcessors(),
      "count.mem.free" -> Runtime.getRuntime().freeMemory(),
      "count.mem.maxMemory" -> Runtime.getRuntime().maxMemory(),
      "count.mem.totalMemory" -> Runtime.getRuntime().totalMemory()
    )

    val roots = File.listRoots()
    val totalSpaceMap = roots.map(root => s"count.fs.total.${root.getAbsolutePath}" -> root.getTotalSpace) toMap
    val freeSpaceMap = roots.map(root => s"count.fs.free.${root.getAbsolutePath}" -> root.getFreeSpace) toMap
    val usuableSpaceMap = roots.map(root => s"count.fs.usuable.${root.getAbsolutePath}" -> root.getUsableSpace) toMap

    baseStats ++ totalSpaceMap ++ freeSpaceMap ++ usuableSpaceMap
  }
}
