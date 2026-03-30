package com.example.rickandmorty.screens.allEpisodes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.repository.EpisodeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AllEpisodesViewModel(
    private val repository: EpisodeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AllEpisodesUiState>(AllEpisodesUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun refreshAllEpisodes(forceRefresh: Boolean = false) = viewModelScope.launch {
        if (forceRefresh) _uiState.update { AllEpisodesUiState.Loading }
        repository.fetchAllEpisodes()
            .onSuccess { episodeList ->
                _uiState.update {
                    AllEpisodesUiState.Success(
                        data = episodeList.groupBy {
                            it.seasonNumber.toString()
                        }.mapKeys {
                            "Season ${it.key}"
                        }
                    )
                }
            }.onFailure {
                _uiState.update { AllEpisodesUiState.Error }
            }
    }
}