package com.darien.vpweek7.soal1.model


data class Card(
    val id: String,
    val tcgPlayerId: String?,
    val name: String,
    val setName: String,
    val cardNumber: String?,
    val rarity: String?,
    val pokemonType: String?,
    val artist: String?,
    val hp: String?,
    val imageUrl: String?,
    val imageLargeUrl: String?,
    val marketPrice: Double?,
    val lowestPrice: Double?,
    val listings: Int?,
    val sellers: Int?,
    val primaryPrinting: String?,
)

data class CardPage(
    val cards: List<Card>,
    val total: Int,
    val hasMore: Boolean,
)