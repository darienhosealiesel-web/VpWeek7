package com.darien.vpweek7.soal2.data.remote

import com.darien.vpweek7.soal2.data.dto.AlbumListResponseDto
import com.darien.vpweek7.soal2.data.dto.ArtistListResponseDto
import com.darien.vpweek7.soal2.data.dto.TrackListResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface AudioDbApiService {

    @GET("search.php")
    suspend fun searchArtist(
        @Query("s") artistName: String,
    ): ArtistListResponseDto

    @GET("searchalbum.php")
    suspend fun searchAlbums(
        @Query("s") artistName: String,
    ): AlbumListResponseDto

    @GET("album.php")
    suspend fun getAlbumDetail(
        @Query("m") albumId: String,
    ): AlbumListResponseDto

    @GET("track.php")
    suspend fun getAlbumTracks(
        @Query("m") albumId: String,
    ): TrackListResponseDto
}