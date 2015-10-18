package com.raduy.scalegro

import akka.actor.ActorRef

/**
 * @author Łukasz Raduj 2015.
 */
case class AuctionFinishedEvent(auctionId: String)
case class StartAuctionEvent()
case class YouWonAuctionEvent(auctionId: ActorRef)

