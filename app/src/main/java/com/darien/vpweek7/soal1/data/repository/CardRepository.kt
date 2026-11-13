package com.darien.vpweek7.soal1.data.repository

import com.darien.vpweek7.soal1.data.remote.PokemonAPIService
import com.darien.vpweek7.soal1.model.CardPage
import com.darien.vpweek7.soal1.util.safeCall

class CardRepository(private val api: PokemonAPIService) {

    private val searchCache = mutableMapOf<String, CardPage>()

    suspend fun searchCards(query: String, limit: Int = 20, offset: Int = 0): Result<CardPage> {
        val key = "$query|$limit|$offset"
        searchCache[key]?.let { return Result.success(it) }
        return safeCall {
            api.searchCards(search = query, limit = limit, offset = offset)
                .toDomain()
                .also { searchCache[key] = it }
        }
    }
}