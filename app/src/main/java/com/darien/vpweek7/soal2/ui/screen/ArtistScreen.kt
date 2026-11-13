package com.darien.vpweek7.soal2.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.darien.vpweek7.soal2.ui.widget.AlbumCard
import com.darien.vpweek7.soal2.ui.widget.BioCard
import com.darien.vpweek7.soal2.ui.widget.HeaderCard
import com.darien.vpweek7.soal2.ui.widget.AppHeader
import com.darien.vpweek7.soal2.ui.widget.ErrorBox
import com.darien.vpweek7.soal2.ui.widget.LoadingBox
import com.darien.vpweek7.soal2.ui.widget.SearchBar
import com.darien.vpweek7.soal2.ui.widget.SectionHeader
import com.darien.vpweek7.soal2.viewmodel.ArtistViewModel

@Composable
fun ArtistScreen(
    onAlbumClick: (String) -> Unit,
    viewModel: ArtistViewModel = viewModel(factory = ArtistViewModel.Factory),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val discography = state.discography

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F1115))
            .statusBarsPadding(),
    ) {
        AppHeader(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp))

        SearchBar(
            query = state.query,
            onQueryChange = viewModel::onQueryChange,
            onSearch = {
                focusManager.clearFocus()
                viewModel.onSearch()
            },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
        )

        when {
            state.isLoading || state.error != null -> Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                if (state.isLoading) {
                    LoadingBox(message = "Memuat data artis...")
                } else {
                    ErrorBox(message = state.error.orEmpty(), onRetry = viewModel::retry)
                }
            }

            discography != null -> LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 4.dp,
                    bottom = 24.dp + navBarPadding,
                ),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item(key = "header", span = { GridItemSpan(maxLineSpan) }) {
                    HeaderCard(artist = discography.artist)
                }

                item(key = "bio", span = { GridItemSpan(maxLineSpan) }) {
                    BioCard(title = "Tentang Artis", text = discography.artist.biography)
                }

                item(key = "section", span = { GridItemSpan(maxLineSpan) }) {
                    SectionHeader(
                        title = "Diskografi Album",
                        badge = "${discography.albums.size} Album",
                        subtitle = "Koleksi rilisan resmi",
                        modifier = Modifier.padding(top = 12.dp),
                    )
                }

                if (discography.albums.isEmpty()) {
                    item(key = "empty", span = { GridItemSpan(maxLineSpan) }) {
                        Text(
                            "Belum ada album untuk artis ini.",
                            color = Color(0xFFA0AAB7),
                            fontSize = 14.sp,
                        )
                    }
                }

                items(discography.albums, key = { it.id }) { album ->
                    AlbumCard(
                        album = album,
                        onClick = { onAlbumClick(album.id) },
                    )
                }
            }
        }
    }
}