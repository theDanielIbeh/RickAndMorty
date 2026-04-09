package com.example.rickandmorty.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.rickandmorty.repository.CharacterRepository

class HomeViewModel(
    characterRepository: CharacterRepository
) : ViewModel() {
    val characters = characterRepository.getCharactersByName()
        .cachedIn(viewModelScope)
}