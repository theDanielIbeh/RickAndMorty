package com.example.rickandmorty.feature.characterepisodes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.core.data.repository.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharacterEpisodesViewModel(
    private val characterRepository: CharacterRepository
) : ViewModel() {
    private var _screenState = MutableStateFlow<ScreenState>(ScreenState.Loading)
    val stateFlow = _screenState.asStateFlow()

    fun fetchCharacter(characterId: Int) = viewModelScope.launch {
        characterRepository.getCharacter(characterId).onSuccess { character ->
            launch {
                characterRepository.getEpisodes(character.episodeIds).onSuccess { episodes ->
                    _screenState.update {
                        return@update ScreenState.Success(character, episodes)
                    }
                }.onFailure {
                    _screenState.update {
                        return@update ScreenState.Error(
                            message = "Whoops, something went wrong"
                        )
                    }
                }
            }
        }.onFailure {
            _screenState.update {
                return@update ScreenState.Error(message = "Whoops, something went wrong")
            }
        }
    }
}