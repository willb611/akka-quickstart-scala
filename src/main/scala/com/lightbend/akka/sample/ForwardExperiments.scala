package com.lightbend.akka.sample

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}

case class SampleMessage(message: String)

object ForwardExperiments extends App {
  val system = ActorSystem("forwardExperiments")
  val router = system.actorOf(Props[RouterActor], "router")
  router ! SampleMessage("test")
}

class RouterActor extends Actor with ActorLogging {
  val child = context.actorOf(Props[SomeActor])
  val child2 = context.actorOf(Props[SomeActor])

  override def receive: Receive = {
    case sampleMessage: SampleMessage ⇒
      log.info("[receive] Got message: {}", sampleMessage)
      child forward sampleMessage
      child2 forward sampleMessage
  }
}

class SomeActor extends Actor with ActorLogging {
  override def unhandled(message: Any): Unit = {
    log.info("[unhandled] got msg: {}, type: {}",
      message, message.getClass)
    super.unhandled(message)
  }

  override def receive: Receive = {
    case sampleMessage: SampleMessage =>
      log.info("[receive] Got message: {}", sampleMessage)
  }
}