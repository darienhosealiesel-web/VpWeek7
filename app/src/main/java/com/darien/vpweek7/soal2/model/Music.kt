package com.darien.vpweek7.soal2.model

data class Artist(
    val id: String,
    val name: String,
    val genre: String,
    val country: String,
    val biography: String,
    val thumbUrl: String?,
    val bannerUrl: String?,
)

data class Album(
    val id: String,
    val title: String,
    val artist: String,
    val year: String,
    val genre: String,
    val coverUrl: String?,
)

data class Track(
    val id: String,
    val number: Int,
    val title: String,
    val genre: String,
    val duration: String,
)

data class AlbumDetail(
    val id: String,
    val title: String,
    val artist: String,
    val year: String,
    val genre: String,
    val description: String,
    val coverUrl: String?,
    val tracks: List<Track>,
)

data class ArtistDiscography(
    val artist: Artist,
    val albums: List<Album>,
)