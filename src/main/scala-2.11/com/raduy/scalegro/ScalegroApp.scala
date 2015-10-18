package com.raduy.scalegro

import akka.actor.{ActorRef, ActorSystem, Props}

/**
 * @author Łukasz Raduj 2015.
 */
object ScalegroApp extends App {
  private val system: ActorSystem = ActorSystem("Scalegro")
  private val scalegro: ActorRef = system.actorOf(Props[AuctionSystem], "auction_system")

  scalegro ! CreateNewAuctionCommand("Google Nexus 5", "Best mobile phone")
  scalegro ! CreateNewAuctionCommand("MacBook Pro", "Good laptop just for you")
  scalegro ! CreateNewAuctionCommand("Blue jeans", "Just old jeans")

  scalegro ! CreateNewBuyerCommand("Lukasz")
  scalegro ! CreateNewBuyerCommand("Jacek")
  scalegro ! CreateNewBuyerCommand("Pawel")

  scalegro ! DoDemoCommand()
  system.awaitTermination()
}
