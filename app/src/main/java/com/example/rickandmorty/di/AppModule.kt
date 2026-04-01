package com.example.rickandmorty.di

import com.example.network.di.networkModule

val appModule = listOf(
    viewModelModule,
    repositoryModule,
    networkModule
)