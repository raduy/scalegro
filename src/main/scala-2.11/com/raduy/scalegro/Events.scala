package com.raduy.scalegro

/**
 * @author Łukasz Raduj 2015.
 */
case class AuctionFinishedEvent(auctionId: String)

case class YouWonAuctionEvent(auctionTitle: String, finalPrice: BigDecimal)
