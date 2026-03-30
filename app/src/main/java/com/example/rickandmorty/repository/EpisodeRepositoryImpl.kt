package com.example.rickandmorty.repository

import com.example.network.ApiOperation
import com.example.network.NetworkDataSource
import com.example.network.model.domain.Episode

class EpisodeRepositoryImpl (private val networkDataSource: NetworkDataSource): EpisodeRepository {
    override suspend fun fetchAllEpisodes(): ApiOperation<List<Episode>> = networkDataSource.getAllEpisodes()
}