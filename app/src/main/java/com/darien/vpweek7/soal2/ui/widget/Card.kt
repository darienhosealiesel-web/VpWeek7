package com.darien.vpweek7.soal2.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.darien.vpweek7.soal2.model.Album
import com.darien.vpweek7.soal2.model.Artist
import com.darien.vpweek7.soal2.model.Track

@Composable
fun HeaderCard(
    artist: Artist,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.6f)
            .clip(RoundedCornerShape(24.dp)),
    ) {
        RemoteImage(
            url = artist.bannerUrl ?: artist.thumbUrl,
            contentDescription = artist.name,
            modifier = Modifier.fillMaxSize(),
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0x000F1115), Color(0xF20F1115)),
                    ),
                ),
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            RemoteImage(
                url = artist.thumbUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color(0xFF21262E), CircleShape),
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = artist.name,
                    color = Color(0xFFFFFFFF),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    if (artist.genre.isNotEmpty()) {
                        Text(
                            text = artist.genre,
                            color = Color(0xFF57B660),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    if (artist.genre.isNotEmpty() && artist.country.isNotEmpty()) {
                        Text("•", color = Color(0xFF7C848E), fontSize = 11.sp)
                    }
                    if (artist.country.isNotEmpty()) {
                        Text(
                            text = artist.country,
                            color = Color(0xFFA0AAB7),
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BioCard(
    title: String,
    text: String,
    modifier: Modifier = Modifier,
) {
    if (text.isBlank()) return

    var expanded by rememberSaveable(text) { mutableStateOf(false) }
    var overflowing by remember(text) { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = title,
            color = Color(0xFFFFFFFF),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = text,
            color = Color(0xFFA0AAB7),
            fontSize = 14.sp,
            lineHeight = 22.sp,
            maxLines = if (expanded) Int.MAX_VALUE else 3,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { result ->
                if (!expanded) overflowing = result.hasVisualOverflow
            },
        )
        if (overflowing) {
            Text(
                text = if (expanded) "Tutup ringkasan" else "Baca selengkapnya",
                color = Color(0xFF57B660),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { expanded = !expanded },
            )
        }
    }
}

@Composable
fun AlbumCard(
    album: Album,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        RemoteImage(
            url = album.coverUrl,
            contentDescription = album.title,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp)),
        )

        Text(
            text = album.title,
            color = Color(0xFFFFFFFF),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = album.year,
                color = Color(0xFF57B660),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = album.genre,
                color = Color(0xFFA0AAB7),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun TrackCard(
    track: Track,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "%02d".format(track.number),
                color = Color(0xFF57B660),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(40.dp),
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = track.title,
                    color = Color(0xFFFFFFFF),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (track.genre.isNotEmpty()) {
                    Text(
                        text = track.genre,
                        color = Color(0xFF7C848E),
                        fontSize = 12.sp,
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(
                    Icons.Outlined.Schedule,
                    contentDescription = null,
                    tint = Color(0xFFA0AAB7),
                    modifier = Modifier.size(14.dp),
                )
                Text(
                    text = track.duration,
                    color = Color(0xFFA0AAB7),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0xFF181B20)),
        )
    }
}