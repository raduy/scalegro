package com.raduy.scalegro

import akka.actor.{ActorLogging, Actor, ActorRef}

/**
 * @author Łukasz Raduj 2015.
 */
class Buyer(name: String, auctions: List[ActorRef]) extends Actor with ActorLogging {

  override def receive: Receive = {
    case YouWonAuctionEvent(auctionTitle: String, finalPrice: BigDecimal) =>
      log.info("User {} won {} auction with final price of {}", name, auctionTitle, finalPrice)
  }
}
