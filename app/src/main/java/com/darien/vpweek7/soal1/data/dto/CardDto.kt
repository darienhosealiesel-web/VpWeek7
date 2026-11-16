package com.darien.vpweek7.soal1.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CardListResponseDto(
    val data: List<Carddto> = emptyList(),
    val metadata: CardMetadataDto? = null,
)

@Serializable
data class CardMetadataDto(
    val total: Int = 0,
    val hasMore: Boolean = false,
)

@Serializable
data class Carddto(
    val id: String,
    val tcgPlayerId: String? = null,
    val name: String,
    val setName: String? = null,
    val cardNumber: String? = null,
    val rarity: String? = null,
    val pokemonType: String? = null,
    val hp: String? = null,
    val artist: String? = null,
    val imageUrl: String? = null,
    val imageCdnUrl800: String? = null,
    val prices: CardPricesDto? = null,
)

@Serializable
data class CardPricesDto(
    val market: Double? = null,
    val low: Double? = null,
    val sellers: Int? = null,
    val listings: Int? = null,
    val primaryPrinting: String? = null,
)