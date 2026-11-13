package com.darien.vpweek7.soal1.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.darien.vpweek7.soal1.data.repository.CardRepository
import com.darien.vpweek7.soal1.data.remote.ServiceLocator
import com.darien.vpweek7.soal1.model.Card
import com.darien.vpweek7.soal1.util.toUserMessage
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SearchUiState(
    val query: String = "",
    val searchedQuery: String = "",
    val cards: List<Card> = emptyList(),
    val total: Int = 0,
    val hasMore: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
)

class SearchViewModel(
    private val repository: CardRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(text: String) {
        _uiState.update { it.copy(query = text) }
    }

    fun onSearch() {
        val q = _uiState.value.query.trim()
        if (q.isBlank()) return
        startSearch(q)
    }

    fun onQuickSearch(name: String) {
        _uiState.update { it.copy(query = name) }
        startSearch(name)
    }

    private fun startSearch(q: String) {
        searchJob?.cancel()
        _uiState.update {
            it.copy(
                searchedQuery = q,
                cards = emptyList(),
                total = 0,
                hasMore = false,
                isLoading = true,
                error = null,
            )
        }
        searchJob = viewModelScope.launch {
            repository.searchCards(q, PAGE_SIZE, 0)
                .onSuccess { page ->
                    _uiState.update {
                        it.copy(
                            cards = page.cards,
                            total = page.total,
                            hasMore = page.hasMore,
                            isLoading = false,
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.toUserMessage()) }
                }
        }
    }

    fun loadMore() {
        val s = _uiState.value
        if (s.isLoading || s.isLoadingMore || !s.hasMore || s.error != null) return

        _uiState.update { it.copy(isLoadingMore = true) }
        viewModelScope.launch {
            repository.searchCards(s.searchedQuery, PAGE_SIZE, s.cards.size)
                .onSuccess { page ->
                    _uiState.update {
                        it.copy(
                            cards = (it.cards + page.cards).distinctBy { c -> c.id },
                            total = page.total,
                            hasMore = page.hasMore,
                            isLoadingMore = false,
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoadingMore = false, error = e.toUserMessage()) }
                }
        }
    }

    fun retry() {
        val s = _uiState.value
        if (s.searchedQuery.isBlank()) return
        _uiState.update { it.copy(error = null) }
        if (s.cards.isEmpty()) startSearch(s.searchedQuery) else loadMore()
    }

    companion object {
        private const val PAGE_SIZE = 20

        val Factory = viewModelFactory {
            initializer { SearchViewModel(ServiceLocator.cardRepository) }
        }
    }
}