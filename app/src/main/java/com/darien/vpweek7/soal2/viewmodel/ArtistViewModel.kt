package com.darien.vpweek7.soal2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.darien.vpweek7.soal2.data.remote.ServiceLocator
import com.darien.vpweek7.soal2.data.repository.MusicRepository
import com.darien.vpweek7.soal2.model.ArtistDiscography
import com.darien.vpweek7.soal2.util.toUserMessage
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ArtistUiState(
    val query: String = DEFAULT_QUERY,
    val searchedQuery: String = "",
    val discography: ArtistDiscography? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)

private const val DEFAULT_QUERY = "Coldplay"

class ArtistViewModel(
    private val repository: MusicRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ArtistUiState())
    val uiState: StateFlow<ArtistUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        startSearch(DEFAULT_QUERY)
    }

    fun onQueryChange(text: String) {
        _uiState.update { it.copy(query = text) }
    }

    fun onSearch() {
        val q = _uiState.value.query.trim()
        if (q.isBlank()) return
        startSearch(q)
    }

    private fun startSearch(q: String) {
        searchJob?.cancel()
        _uiState.update {
            it.copy(
                searchedQuery = q,
                discography = null,
                isLoading = true,
                error = null,
            )
        }
        searchJob = viewModelScope.launch {
            repository.getDiscography(q)
                .onSuccess { result ->
                    _uiState.update { it.copy(discography = result, isLoading = false) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.toUserMessage()) }
                }
        }
    }

    fun retry() {
        val q = _uiState.value.searchedQuery
        if (q.isBlank()) return
        startSearch(q)
    }

    companion object {
        val Factory = viewModelFactory {
            initializer { ArtistViewModel(ServiceLocator.musicRepository) }
        }
    }
}