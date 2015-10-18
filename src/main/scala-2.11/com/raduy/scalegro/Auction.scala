package com.raduy.scalegro

import java.util.concurrent.TimeUnit

import akka.actor.{ActorLogging, ActorRef, FSM}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration

/**
 * @author Łukasz Raduj 2015.
 */
class Auction(title: String, description: String) extends FSM[AuctionState, AuctionData] with ActorLogging {
  var price: BigDecimal = 0

  startWith(Created, NoOffers)

  when(Created) {
    case Event(BidCommand(offer: BigDecimal, bidder: ActorRef), NoOffers) =>
      log.debug("First offer received! Offer value:" + offer)
      goto(Activated) using Offer(offer, bidder)

    case Event(FinishAuctionCommand, NoOffers) =>
      //start DeleteTimer
      import ExecutionContext.Implicits.global
      context.system.scheduler.scheduleOnce(Duration(5, TimeUnit.SECONDS), self, DeleteSoldAuctionCommand)

      goto(Ignored)
  }

  when(Ignored) {
    case Event(_ , _) =>
      stay()
  }

  when(Activated) {
    case Event(BidCommand(offer: BigDecimal, bidder: ActorRef), o: Offer) =>
      log.debug("New offer in on-going auction! Offer value:" + offer)
      stay() using (if (offer > o.price) Offer(offer, bidder) else o)

    case Event(FinishAuctionCommand, o: Offer) =>
      log.debug("Finishing auction! Final Price:" + o.price)
      //notify buyer
      o.bidder ! YouWonAuctionEvent(self)

      //start DeleteTimer
      import ExecutionContext.Implicits.global
      context.system.scheduler.scheduleOnce(Duration(5, TimeUnit.SECONDS), self, DeleteSoldAuctionCommand)

      goto(Sold)
  }

  when(Sold) {
    case Event(DeleteSoldAuctionCommand(), o: Offer) =>
      log.debug("Deleting sold auction")
      stay()
  }

  override def toString: String = {
    title + " " + description
  }
}
