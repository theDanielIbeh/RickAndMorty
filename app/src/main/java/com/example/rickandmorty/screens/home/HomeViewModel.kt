package com.example.rickandmorty.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.network.model.domain.CharacterPage
import com.example.rickandmorty.repository.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val characterRepository: CharacterRepository
) : ViewModel() {
    private val _viewState = MutableStateFlow<HomeScreenViewState>(HomeScreenViewState.Loading)
    val viewState: StateFlow<HomeScreenViewState> = _viewState.asStateFlow()

    private val fetchedCharacterPages = mutableListOf<CharacterPage>()

    fun fetchInitialPage() = viewModelScope.launch {
        if (fetchedCharacterPages.isNotEmpty()) return@launch
        val initialPage = characterRepository.fetchCharacterPage(page = 1)
        initialPage.onSuccess { characterPage ->
            fetchedCharacterPages.clear()
            fetchedCharacterPages.add(characterPage)

            _viewState.update {
                return@update HomeScreenViewState.GridDisplay(characters = characterPage.characters)
            }
        }.onFailure {
            // todo
        }
    }

    fun fetchNextPage() = viewModelScope.launch {
        val nextPageIndex = fetchedCharacterPages.size + 1
        characterRepository.fetchCharacterPage(page = nextPageIndex).onSuccess { characterPage ->
            fetchedCharacterPages.add(characterPage)
            _viewState.update { currentState ->
                val currentCharacters = (currentState as? HomeScreenViewState.GridDisplay)?.characters ?: emptyList()
                return@update HomeScreenViewState.GridDisplay(characters = currentCharacters + characterPage.characters)
            }
        }.onFailure {
            // todo
        }
    }
}