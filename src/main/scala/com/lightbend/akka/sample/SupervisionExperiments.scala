package com.lightbend.akka.sample

import akka.actor.{Actor, ActorSystem, Props}

object SupervisionExperiments extends App {
  val system = ActorSystem("supervisionExperiments")
  val supervisingActor = system.actorOf(Props[SupervisingActor], "supervising-actor")
  supervisingActor ! "failChild"

}

class SupervisingActor extends Actor {
  val child = context.actorOf(Props[SupervisedActor], "supervised-actor")

  override def receive: Receive = {
    case "failChild" ⇒ child ! "fail"
  }
}

class SupervisedActor extends Actor {
  override def preStart(): Unit = println("supervised actor started")
  override def postStop(): Unit = println("supervised actor stopped")

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    println("preRestart")
    super.preRestart(reason, message)
  }

  override def receive: Receive = {
    case "fail" ⇒
      println("supervised actor fails now")
      throw new Exception("I failed!")
  }
}

