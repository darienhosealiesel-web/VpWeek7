package com.darien.vpweek7.soal1.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CatchingPokemon
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.darien.vpweek7.soal1.viewmodel.SearchViewModel
import com.darien.vpweek7.soal1.model.Card
import com.darien.vpweek7.soal1.ui.widget.CardDetailSheet
import com.darien.vpweek7.soal1.ui.widget.CardItem
import com.darien.vpweek7.soal1.ui.widget.ErrorBox
import com.darien.vpweek7.soal1.ui.widget.FilterChipRow
import com.darien.vpweek7.soal1.ui.widget.LoadingBox
import com.darien.vpweek7.soal1.ui.widget.SearchBar

import kotlin.collections.isNotEmpty

private val QuickSearches = listOf("Charizard", "Pikachu", "Mewtwo", "Gengar")

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = viewModel(factory = SearchViewModel.Factory),
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value
    val gridState = rememberLazyGridState()
    var selectedCard by remember { mutableStateOf<Card?>(null) }

    val nearEnd by remember {
        derivedStateOf {
            val info = gridState.layoutInfo
            val last = info.visibleItemsInfo.lastOrNull()?.index ?: 0
            info.totalItemsCount > 0 && last >= info.totalItemsCount - 4
        }
    }
    LaunchedEffect(nearEnd, state.cards.size) {
        if (nearEnd) viewModel.loadMore()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F1115)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF151A22))
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(Color(0xFFE5483B), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.CatchingPokemon,
                    contentDescription = "Logo",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp),
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "PokéTracker",
                    color = Color(0xFFF2F4F7),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    "Real-time TCG Card Valuation",
                    color = Color(0xFF8B93A3),
                    fontSize = 11.sp,
                )
            }
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color(0x1F3DD68C))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(Color(0xFF3DD68C), CircleShape),
                )
                Text(
                    "LIVE API",
                    color = Color(0xFF3DD68C),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            state = gridState,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item(key = "search", span = { GridItemSpan(maxLineSpan) }) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SearchBar(
                        query = state.query,
                        onQueryChange = viewModel::onQueryChange,
                        onSearch = viewModel::onSearch,
                    )
                    FilterChipRow(
                        options = QuickSearches,
                        onSelect = viewModel::onQuickSearch,
                    )
                }
            }

            item(key = "results", span = { GridItemSpan(maxLineSpan) }) {
                when {
                    state.searchedQuery.isBlank() -> Text(
                        "Search a card or pick a quick search above.",
                        color = Color(0xFF8B93A3),
                        fontSize = 14.sp,
                    )
                    state.cards.isNotEmpty() -> Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            "Market Results (${state.total})",
                            color = Color(0xFFF2F4F7),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            "USD • Near Mint",
                            color = Color(0xFF8B93A3),
                            fontSize = 11.sp,
                        )
                    }
                }
            }

            when {
                state.isLoading -> item(key = "loading", span = { GridItemSpan(maxLineSpan) }) {
                    LoadingBox()
                }
                state.error != null && state.cards.isEmpty() -> item(key = "error", span = { GridItemSpan(maxLineSpan) }) {
                    ErrorBox(message = state.error, onRetry = viewModel::retry)
                }
            }

            items(state.cards, key = { it.id }) { card ->
                CardItem(
                    card = card,
                    onClick = { selectedCard = card },
                )
            }

            item(key = "footer", span = { GridItemSpan(maxLineSpan) }) {
                when {
                    state.error != null && state.cards.isNotEmpty() -> ErrorBox(
                        message = state.error,
                        onRetry = viewModel::retry,
                    )
                    state.isLoadingMore -> LoadingBox()
                    !state.hasMore && state.cards.isNotEmpty() -> Text(
                        "No more results",
                        color = Color(0xFF8B93A3),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }
        }
    }

    selectedCard?.let { card ->
        CardDetailSheet(
            card = card,
            onDismiss = { selectedCard = null },
        )
    }
}