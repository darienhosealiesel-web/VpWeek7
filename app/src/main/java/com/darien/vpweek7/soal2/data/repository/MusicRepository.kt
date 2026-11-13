package com.darien.vpweek7.soal2.data.repository



import com.darien.vpweek7.soal2.data.remote.AudioDbApiService
import com.darien.vpweek7.soal2.model.AlbumDetail
import com.darien.vpweek7.soal2.model.ArtistDiscography
import com.darien.vpweek7.soal2.util.safeCall

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class MusicRepository(private val api: AudioDbApiService) {

    private val discographyCache = mutableMapOf<String, ArtistDiscography>()
    private val detailCache = mutableMapOf<String, AlbumDetail>()

    suspend fun getDiscography(artistName: String): Result<ArtistDiscography> {
        val key = artistName.trim().lowercase()
        discographyCache[key]?.let { return Result.success(it) }
        return safeCall  {
            val artist = api.searchArtist(artistName.trim())
                .artists
                ?.firstOrNull()
                ?: throw NoSuchElementException("Artis \"${artistName.trim()}\" tidak ditemukan.")
            val albums = api.searchAlbums(artist.strArtist)
                .album
                .orEmpty()
            ArtistDiscography(
                artist = artist.toDomain(),
                albums = albums.map { it.toDomain() },
            ).also { discographyCache[key] = it }
        }
    }

    suspend fun getAlbumDetail(albumId: String): Result<AlbumDetail> {
        detailCache[albumId]?.let { return Result.success(it) }
        return safeCall {
            coroutineScope {
                val albumRequest = async { api.getAlbumDetail(albumId) }
                val trackRequest = async { api.getAlbumTracks(albumId) }
                val album = albumRequest.await()
                    .album
                    ?.firstOrNull()
                    ?: throw NoSuchElementException("Album tidak ditemukan.")
                album.toDetail(trackRequest.await().track.orEmpty())
                    .also { detailCache[albumId] = it }
            }
        }
    }
}