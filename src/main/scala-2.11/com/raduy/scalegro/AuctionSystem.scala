package com.raduy.scalegro

import java.util.Random
import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.event.LoggingReceive

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration

/**
 * @author Łukasz Raduj 2015.
 */
class AuctionSystem extends Actor with ActorLogging {
  var auctions: List[ActorRef] = List()
  var buyers: List[ActorRef] = List()

  override def receive: Receive = LoggingReceive {
    case CreateNewAuctionCommand(title: String, description: String) =>
      val newAuction: ActorRef = context.actorOf(Props(new Auction(title, description)))
      auctions = newAuction :: auctions

      import ExecutionContext.Implicits.global
      context.system.scheduler.scheduleOnce(Duration(5, TimeUnit.MINUTES), newAuction, FinishAuctionCommand)

      log.debug("New auction created! Title: " + title)
    case CreateNewBuyerCommand(name: String) =>
      val newBuyer: ActorRef = context.actorOf(Props(new Buyer(name, auctions)))
      buyers = newBuyer :: buyers

      log.debug("New buyer created! Name: " + name)
    case DoDemoCommand() =>
      val rand = new Random()
      var offer = BigDecimal(1.0)
      for (a <- 1 to 20) {
        val auction = auctions(rand.nextInt(auctions.size))
        val buyer = buyers(rand.nextInt(buyers.size))

        auction ! BidCommand(offer, buyer)
        offer = offer + 1.0
      }

    case ListAllAuctionsQuery() =>
      sender() ! auctions
    case AuctionFinishedEvent(auctionId: String) =>
  }
}
