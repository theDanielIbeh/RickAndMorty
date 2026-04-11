package com.example.rickandmorty.core.data.di

import com.example.rickandmorty.core.data.repository.CharacterRepository
import com.example.rickandmorty.core.data.repository.CharacterRepositoryImpl
import com.example.rickandmorty.core.data.repository.EpisodeRepository
import com.example.rickandmorty.core.data.repository.EpisodeRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::CharacterRepositoryImpl) { bind<CharacterRepository>() }
    singleOf(::EpisodeRepositoryImpl) { bind<EpisodeRepository>() }
}