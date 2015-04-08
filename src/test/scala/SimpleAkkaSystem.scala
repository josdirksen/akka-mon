import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import org.akkamon.core.instruments.{CounterTrait, TimingTrait, LoggingTrait}
import org.akkamon.core.tracing.{Envelope, EnvelopingTrait}

case class Msg(message: String ="hello")

object Main extends App {

  val system = ActorSystem("HelloSystem")
  val helloActor = system.actorOf(Props[HelloActor], name = "helloactor")

  helloActor ! new Msg("s") with Envelope
  helloActor ! new Msg("s") with Envelope
  helloActor ! "hello"
  helloActor ! "hello"
  helloActor ! "buenos dias"
  helloActor ! "buenos dias"
  helloActor ! "buenos dias"
  helloActor ! "buenos dias"

  while (true) {
    Thread.sleep(1000);

    helloActor ! new Msg("hello") with Envelope
    helloActor ! "hello"
    helloActor ! "buenos dias"
  }

}

class HelloActor extends Actor with LoggingTrait with TimingTrait with CounterTrait {

  def receive: Receive = {
    case "hello" => println("hello back at you")
    case msg: Msg => println(msg.message)
    case _ => println("huh?")
  }
}
