package com.darien.vpweek7.soal2.util

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
        404 -> "Data tidak ditemukan."
        429 -> "Terlalu banyak permintaan. Tunggu sebentar lalu coba lagi."
        503 -> "Layanan sedang tidak tersedia. Coba lagi nanti."
        else -> "Server bermasalah (kode ${code()})."
    }
    is IOException -> message ?: "Tidak ada koneksi internet."
    else -> message ?: "Terjadi kesalahan."
}

