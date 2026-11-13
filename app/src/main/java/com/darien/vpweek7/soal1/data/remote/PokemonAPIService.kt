package com.darien.vpweek7.soal1.data.remote

import com.darien.vpweek7.soal1.data.dto.CardListResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonAPIService {

    @GET("cards")
    suspend fun searchCards(
        @Query("search") search: String,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
    ): CardListResponseDto
}