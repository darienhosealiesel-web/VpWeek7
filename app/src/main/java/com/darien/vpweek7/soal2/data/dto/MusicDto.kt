package com.darien.vpweek7.soal2.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ArtistListResponseDto(
    val artists: List<ArtistDto>? = null,
)

@Serializable
data class AlbumListResponseDto(
    val album: List<AlbumDto>? = null,
)

@Serializable
data class TrackListResponseDto(
    val track: List<TrackDto>? = null,
)

@Serializable
data class ArtistDto(
    val idArtist: String,
    val strArtist: String,
    val strGenre: String? = null,
    val strCountry: String? = null,
    val strBiography: String? = null,
    val strArtistThumb: String? = null,
    val strArtistFanart: String? = null,
    val strArtistBanner: String? = null,
)

@Serializable
data class AlbumDto(
    val idAlbum: String,
    val strAlbum: String,
    val strArtist: String? = null,
    val intYearReleased: String? = null,
    val strGenre: String? = null,
    val strAlbumThumb: String? = null,
    val strDescription: String? = null,
)

@Serializable
data class TrackDto(
    val idTrack: String,
    val strTrack: String,
    val intDuration: String? = null,
    val intTrackNumber: String? = null,
    val strGenre: String? = null,
)
