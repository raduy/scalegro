package com.raduy.scalegro

import akka.actor.ActorRef

/**
 * Created by Łukasz Raduj on 10/17/15.
 */
case class CreateNewAuctionCommand(title: String, description: String)
case class CreateNewBuyerCommand(name: String)
case class FinishAuctionCommand()
case class BidCommand(offer: BigDecimal, bidder: ActorRef)
case class DoDemoCommand()
