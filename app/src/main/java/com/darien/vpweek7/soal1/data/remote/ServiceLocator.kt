package com.darien.vpweek7.soal1.data.remote

import com.darien.vpweek7.soal1.data.remote.APIKeyInterceptor
import com.darien.vpweek7.soal1.data.remote.PokemonAPIService
import com.darien.vpweek7.soal1.data.repository.CardRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlin.jvm.java

object ServiceLocator {

    private const val BASE_URL = "https://www.pokemonpricetracker.com/api/v2/"
    private const val API_KEY = "pokeprice_free_0d71842c5dc3f1f9635e831d12568494d6d028e6e854741b"

    private val json = Json { ignoreUnknownKeys = true }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(APIKeyInterceptor(API_KEY))
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .build()
    }

    private val api: PokemonAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(PokemonAPIService::class.java)
    }

    val cardRepository: CardRepository by lazy { CardRepository(api) }
}