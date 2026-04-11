package com.example.rickandmorty.core.data.repository

import com.example.rickandmorty.core.network.ApiOperation
import com.example.rickandmorty.core.network.NetworkDataSource
import com.example.rickandmorty.core.network.model.domain.Episode

class EpisodeRepositoryImpl (private val networkDataSource: NetworkDataSource): EpisodeRepository {
    override suspend fun fetchAllEpisodes(): ApiOperation<List<Episode>> = networkDataSource.getAllEpisodes()
}