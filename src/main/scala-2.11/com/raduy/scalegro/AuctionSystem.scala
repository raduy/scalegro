package com.raduy.scalegro

import java.util.Random

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.event.LoggingReceive

/**
 * @author Łukasz Raduj 2015.
 */
class AuctionSystem extends Actor with ActorLogging {
  var auctions: List[ActorRef] = List()
  var buyers: List[ActorRef] = List()

  override def receive: Receive = LoggingReceive {
    case CreateNewAuctionCommand(title: String, description: String) =>
      val newAuction: ActorRef = context.actorOf(Props(new Auction(title, description)), title.replace(" ", "_"))
      auctions = newAuction :: auctions

      log.debug("New auction created! Title: {}", title)
    case CreateNewBuyerCommand(name: String) =>
      val newBuyer: ActorRef = context.actorOf(Props(new Buyer(name, auctions)))
      buyers = newBuyer :: buyers

      log.debug("New buyer created! Name: {}", name)
    case DoDemoCommand() =>
      val rand = new Random()
      var offer = BigDecimal(1.0)
      for (a <- 1 to 20) {
        val auction = auctions(rand.nextInt(auctions.size))
        val buyer = buyers(rand.nextInt(buyers.size))

        auction ! BidCommand(offer, buyer)
        offer = offer + 1.0
        Thread sleep (rand.nextInt(1000) + 500)
      }
  }
}
