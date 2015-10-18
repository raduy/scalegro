package com.raduy.scalegro

import akka.actor.ActorRef

/**
 * @author Łukasz Raduj 2015.
 */
case class CreateNewAuctionCommand(title: String, description: String)

case class CreateNewBuyerCommand(name: String)

case class FinishAuctionCommand()

case class BidCommand(offer: BigDecimal, bidder: ActorRef)

case class DoDemoCommand()

case class DeleteSoldAuctionCommand()

