package com.example.rickandmorty.di

import com.example.network.NetworkDataSource
import com.example.network.ktor.KtorNetworkDataSource
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val networkModule = module {
    singleOf(::KtorNetworkDataSource) {bind<NetworkDataSource>()}
}