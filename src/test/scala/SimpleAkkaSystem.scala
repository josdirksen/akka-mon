import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import org.akkamon.core.instruments.{CounterTrait, TimingTrait, LoggingTrait}

object Main extends App {

  val system = ActorSystem("HelloSystem")
  val helloActor = system.actorOf(Props[HelloActor], name = "helloactor")
  helloActor ! "hello"
  helloActor ! "hello"
  helloActor ! "hello"
  helloActor ! "hello"
  helloActor ! "buenos dias"
  helloActor ! "buenos dias"
  helloActor ! "buenos dias"
  helloActor ! "buenos dias"

  Thread.sleep(3000);

  helloActor ! "hello"
  helloActor ! "hello"
  helloActor ! "buenos dias"
}

class HelloActor extends Actor with LoggingTrait with TimingTrait with CounterTrait {

  def receive: Receive = {
    case "hello" => println("hello back at you")
    case _ => println("huh?")
  }
}
