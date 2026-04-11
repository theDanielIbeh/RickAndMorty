package com.example.rickandmorty.di

import com.example.rickandmorty.core.data.di.repositoryModule
import com.example.rickandmorty.core.network.di.networkModule

val appModule = listOf(
    viewModelModule,
    repositoryModule,
    networkModule
)