package com.darien.vpweek7.soal2.data.repository

import com.darien.vpweek7.soal2.data.dto.AlbumDto
import com.darien.vpweek7.soal2.data.dto.ArtistDto
import com.darien.vpweek7.soal2.data.dto.TrackDto
import com.darien.vpweek7.soal2.model.Album
import com.darien.vpweek7.soal2.model.AlbumDetail
import com.darien.vpweek7.soal2.model.Artist
import com.darien.vpweek7.soal2.model.Track

private fun String?.orNull() = this?.takeIf { it.isNotBlank() }

private fun String?.toDuration(): String {
    val seconds = (this?.toLongOrNull() ?: 0L) / 1000
    if (seconds <= 0) return "--:--"
    return "%02d:%02d".format(seconds / 60, seconds % 60)
}

fun ArtistDto.toDomain() = Artist(
    id = idArtist,
    name = strArtist,
    genre = strGenre.orEmpty(),
    country = strCountry.orEmpty(),
    biography = strBiography.orEmpty().trim(),
    thumbUrl = strArtistThumb.orNull(),
    bannerUrl = strArtistFanart.orNull() ?: strArtistBanner.orNull(),
)

fun AlbumDto.toDomain() = Album(
    id = idAlbum,
    title = strAlbum,
    artist = strArtist.orEmpty(),
    year = intYearReleased.orEmpty(),
    genre = strGenre.orEmpty(),
    coverUrl = strAlbumThumb.orNull(),
)

fun TrackDto.toDomain(albumGenre: String) = Track(
    id = idTrack,
    number = intTrackNumber?.toIntOrNull() ?: 0,
    title = strTrack,
    genre = strGenre.orNull() ?: albumGenre,
    duration = intDuration.toDuration(),
)

fun AlbumDto.toDetail(tracks: List<TrackDto>) = AlbumDetail(
    id = idAlbum,
    title = strAlbum,
    artist = strArtist.orEmpty(),
    year = intYearReleased.orEmpty(),
    genre = strGenre.orEmpty(),
    description = strDescription.orEmpty().trim(),
    coverUrl = strAlbumThumb.orNull(),
    tracks = tracks
        .map { it.toDomain(strGenre.orEmpty()) }
        .sortedBy { it.number },
)