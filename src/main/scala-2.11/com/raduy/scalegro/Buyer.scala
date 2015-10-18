package com.raduy.scalegro

import akka.actor.{Actor, ActorRef}

/**
 * Created by Łukasz Raduj on 10/17/15.
 */
class Buyer(name: String, auctions: List[ActorRef]) extends Actor {
  var myOffer: BigDecimal = 0.0

  override def receive: Receive = {
    case BidCommand(offer: BigDecimal, bidder: ActorRef) =>
//      myOffer = offer
//      auctionSystem ! ListAllAuctionsQuery()
//    case auctions2: List[ActorRef] =>
//      auctions = auctions2
//      auctions.head ! BidCommand(myOffer)
  }
}
