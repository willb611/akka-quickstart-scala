package com.lightbend.akka.sample

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}

class StartStopActor1 extends Actor with ActorLogging {
  override def preStart(): Unit = {
    log.info("first started")
    context.actorOf(Props[StartStopActor2], "second")
  }
  override def postStop(): Unit = log.info("first stopped")

  override def receive: Receive = {
    case "stop" ⇒ context.stop(self)
  }
}

class StartStopActor2 extends Actor with ActorLogging {
  override def preStart(): Unit = log.info("second started")
  override def postStop(): Unit = log.info("second stopped")

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    log.info("[preRestart] received, delegating to super..")
    super.preRestart(reason, message)
  }

  override def postRestart(reason: Throwable): Unit = {
    log.info("[postRestart] received, delegating to super..")
    super.postRestart(reason)
  }

  // Actor.emptyBehavior is a useful placeholder when we don't
  // want to handle any messages in the actor.
  override def receive: Receive = Actor.emptyBehavior
}

object StartStopExperiments extends App {
  val system = ActorSystem("testSystem")

  val first = system.actorOf(Props[StartStopActor1], "first")
  first ! "stop"
}