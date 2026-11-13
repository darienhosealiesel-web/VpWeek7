package com.darien.vpweek7.soal1.util

import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException
import retrofit2.HttpException

suspend fun <T> safeCall(block: suspend () -> T): Result<T> =
    try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }

fun Throwable.toUserMessage(): String = when (this) {
    is HttpException -> when (code()) {
        401 -> "API key tidak valid. Periksa API_KEY di ApiConfig.kt."
        404 -> "Data tidak ditemukan."
        else -> "Server bermasalah (kode ${code()})."
    }
    is IOException -> "Tidak ada koneksi internet."
    else -> message ?: "Terjadi kesalahan."
}