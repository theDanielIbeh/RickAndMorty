package com.example.rickandmorty.di

import com.example.rickandmorty.repository.CharacterRepository
import com.example.rickandmorty.repository.CharacterRepositoryImpl
import com.example.rickandmorty.repository.EpisodeRepository
import com.example.rickandmorty.repository.EpisodeRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::CharacterRepositoryImpl) { bind<CharacterRepository>() }
    singleOf(::EpisodeRepositoryImpl) { bind<EpisodeRepository>() }
}