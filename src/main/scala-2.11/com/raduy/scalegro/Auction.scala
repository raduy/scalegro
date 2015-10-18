package com.raduy.scalegro

import akka.actor.{ActorRef, Actor}
import akka.event.LoggingReceive

/**
 * @author Łukasz Raduj 2015.
 */
//todo use state machine
class Auction(title: String, description: String) extends Actor {
  var price: BigDecimal = 0

  override def receive: Receive = LoggingReceive {
    case StartAuctionEvent =>
      println("starting auction")
      println(title)
      println(System.currentTimeMillis())
    case BidCommand(offer: BigDecimal, bidder: ActorRef) =>
//      println("got new offer!")

    case FinishAuctionCommand() =>
      println("Auction finished! " + "Final price:" + price)
  }

  override def toString: String = {
    title + " " + description
  }
}
