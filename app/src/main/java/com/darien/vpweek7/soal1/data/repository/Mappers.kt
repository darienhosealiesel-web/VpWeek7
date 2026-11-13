package com.darien.vpweek7.soal1.data.repository


import com.darien.vpweek7.soal1.data.dto.CardDto
import com.darien.vpweek7.soal1.data.dto.CardListResponseDto
import com.darien.vpweek7.soal1.model.Card
import com.darien.vpweek7.soal1.model.CardPage

fun CardDto.toDomain() = Card(
    id = id,
    tcgPlayerId = tcgPlayerId,
    name = name,
    setName = setName.orEmpty(),
    cardNumber = cardNumber,
    rarity = rarity,
    pokemonType = pokemonType,
    artist = artist,
    hp = hp,
    imageUrl = imageUrl,
    imageLargeUrl = imageCdnUrl800 ?: imageUrl,
    marketPrice = prices?.market,
    lowestPrice = prices?.low,
    listings = prices?.listings,
    sellers = prices?.sellers,
    primaryPrinting = prices?.primaryPrinting,
)

fun CardListResponseDto.toDomain() = CardPage(
    cards = data.map { it.toDomain() },
    total = metadata?.total ?: data.size,
    hasMore = metadata?.hasMore ?: false,
)