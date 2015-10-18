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

  override def preStart(): Unit = {
    import context.dispatcher
    context.system.scheduler.scheduleOnce(Duration(30, TimeUnit.SECONDS), self, FinishAuctionCommand)
    super.preStart()
  }

  def deleteItem() = {
    log.debug("Deleting sold auction")
    context.stop(self)
  }

  /**
   * Finite State Machine definition
   */
  startWith(Created, NoOffer)

  when(Created) {
    case Event(BidCommand(offer: BigDecimal, bidder: ActorRef), NoOffer) =>
      log.debug("First offer received! Offer value:" + offer)
      goto(Activated) using Offer(offer, bidder)

    case Event(FinishAuctionCommand, NoOffer) =>
      //start DeleteTimer
      import ExecutionContext.Implicits.global
      context.system.scheduler.scheduleOnce(Duration(5, TimeUnit.SECONDS), self, DeleteSoldAuctionCommand)

      goto(Ignored) using NoOffer
  }

  when(Ignored) {
    case Event(DeleteSoldAuctionCommand, NoOffer) =>
      deleteItem()
      stop(FSM.Normal)
  }

  when(Activated) {
    case Event(BidCommand(offer: BigDecimal, bidder: ActorRef), o: Offer) =>
      log.debug("New offer in on-going auction! Offer value:" + offer)
      stay() using (if (offer > o.price) Offer(offer, bidder) else o)

    case Event(FinishAuctionCommand, o: Offer) =>
      log.debug("Finishing auction! Final Price:" + o.price)
      //notify buyer
      o.bidder ! YouWonAuctionEvent(title, o.price)

      //start DeleteTimer
      import context.dispatcher
      context.system.scheduler.scheduleOnce(Duration(3, TimeUnit.SECONDS), self, DeleteSoldAuctionCommand)

      goto(Sold) using o
  }

  when(Sold) {
    case Event(DeleteSoldAuctionCommand, o: Offer) =>
      deleteItem()
      stop(FSM.Normal)
  }

  override def toString: String = {
    title + " " + description
  }
}
