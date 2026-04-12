package com.example.rickandmorty.di

import com.example.rickandmorty.feature.characterdetails.CharacterDetailsViewModel
import com.example.rickandmorty.feature.characterepisodes.CharacterEpisodesViewModel
import com.example.rickandmorty.feature.episodes.AllEpisodesViewModel
import com.example.rickandmorty.feature.home.home.HomeViewModel
import com.example.rickandmorty.feature.search.SearchViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::AllEpisodesViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::CharacterDetailsViewModel)
    viewModelOf(::CharacterEpisodesViewModel)
}