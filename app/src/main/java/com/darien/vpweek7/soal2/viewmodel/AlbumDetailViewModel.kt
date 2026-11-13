package com.darien.vpweek7.soal2.viewmodel

import kotlin.onSuccess



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.darien.vpweek7.soal2.data.remote.ServiceLocator
import com.darien.vpweek7.soal2.data.repository.MusicRepository
import com.darien.vpweek7.soal2.model.AlbumDetail
import com.darien.vpweek7.soal2.util.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AlbumDetailUiState {
    data object Loading : AlbumDetailUiState
    data class Success(val album: AlbumDetail) : AlbumDetailUiState
    data class Error(val message: String) : AlbumDetailUiState
}

class AlbumDetailViewModel(
    private val albumId: String,
    private val repository: MusicRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<AlbumDetailUiState>(AlbumDetailUiState.Loading)
    val uiState: StateFlow<AlbumDetailUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        _uiState.value = AlbumDetailUiState.Loading
        viewModelScope.launch {
            repository.getAlbumDetail(albumId)
                .onSuccess { _uiState.value = AlbumDetailUiState.Success(it) }
                .onFailure { _uiState.value = AlbumDetailUiState.Error(it.toUserMessage()) }
        }
    }

    companion object {
        fun factory(albumId: String) = viewModelFactory {
            initializer {
                AlbumDetailViewModel(albumId, ServiceLocator.musicRepository)
            }
        }
    }
}