package com.example.rickandmorty.di

import com.example.rickandmorty.screens.allEpisodes.AllEpisodesViewModel
import com.example.rickandmorty.screens.characterDetails.CharacterDetailsViewModel
import com.example.rickandmorty.screens.characterEpisodes.CharacterEpisodesViewModel
import com.example.rickandmorty.screens.home.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::AllEpisodesViewModel)
    viewModelOf(::CharacterDetailsViewModel)
    viewModelOf(::CharacterEpisodesViewModel)
}