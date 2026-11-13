package com.darien.vpweek7.soal2.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.darien.vpweek7.soal2.ui.widget.BioCard
import com.darien.vpweek7.soal2.ui.widget.ErrorBox
import com.darien.vpweek7.soal2.ui.widget.LoadingBox
import com.darien.vpweek7.soal2.ui.widget.RemoteImage
import com.darien.vpweek7.soal2.ui.widget.SectionHeader
import com.darien.vpweek7.soal2.ui.widget.TrackCard
import com.darien.vpweek7.soal2.viewmodel.AlbumDetailUiState
import com.darien.vpweek7.soal2.viewmodel.AlbumDetailViewModel

@Composable
fun AlbumDetailScreen(
    albumId: String,
    onBack: () -> Unit,
    viewModel: AlbumDetailViewModel = viewModel(
        key = albumId,
        factory = AlbumDetailViewModel.factory(albumId),
    ),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F1115))
            .statusBarsPadding(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali",
                    tint = Color(0xFFFFFFFF),
                )
            }
            Text(
                "Detail Album",
                color = Color(0xFFFFFFFF),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        when (val s = state) {
            AlbumDetailUiState.Loading -> Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                LoadingBox(message = "Memuat detail album & track list...")
            }

            is AlbumDetailUiState.Error -> Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                ErrorBox(message = s.message, onRetry = viewModel::load)
            }

            is AlbumDetailUiState.Success -> LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 24.dp + navBarPadding,
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item(key = "cover") {
                    RemoteImage(
                        url = s.album.coverUrl,
                        contentDescription = s.album.title,
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .aspectRatio(1f)
                            .shadow(
                                elevation = 32.dp,
                                shape = RoundedCornerShape(24.dp),
                                ambientColor = Color(0xFF57B660),
                                spotColor = Color(0xFF57B660),
                            )
                            .clip(RoundedCornerShape(24.dp)),
                    )
                }

                item(key = "info") {
                    Column(
                        modifier = Modifier.padding(top = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = s.album.title,
                            color = Color(0xFFFFFFFF),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = s.album.artist,
                            color = Color(0xFF57B660),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Row(
                            modifier = Modifier.padding(top = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            listOf(
                                if (s.album.year.isNotEmpty()) "Tahun ${s.album.year}" else "",
                                s.album.genre,
                            )
                                .filter { it.isNotEmpty() }
                                .forEach { label ->
                                    Text(
                                        text = label,
                                        color = Color(0xFFA0AAB7),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0xFF17191F))
                                            .padding(horizontal = 14.dp, vertical = 8.dp),
                                    )
                                }
                        }
                    }
                }

                item(key = "description") {
                    BioCard(
                        title = "Deskripsi Album",
                        text = s.album.description,
                        modifier = Modifier.padding(top = 24.dp),
                    )
                }

                item(key = "section") {
                    SectionHeader(
                        title = "Daftar Lagu",
                        badge = "${s.album.tracks.size} Lagu",
                        icon = Icons.Filled.MusicNote,
                        modifier = Modifier.padding(top = 28.dp, bottom = 4.dp),
                    )
                }

                if (s.album.tracks.isEmpty()) {
                    item(key = "empty") {
                        Text(
                            "Belum ada daftar lagu untuk album ini.",
                            color = Color(0xFFA0AAB7),
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 16.dp),
                        )
                    }
                }

                items(s.album.tracks, key = { it.id }) { track ->
                    TrackCard(track = track)
                }
            }
        }
    }
}