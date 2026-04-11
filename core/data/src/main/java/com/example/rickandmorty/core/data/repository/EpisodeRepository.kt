package com.example.rickandmorty.core.data.repository

import com.example.rickandmorty.core.network.ApiOperation
import com.example.rickandmorty.core.network.model.domain.Episode

interface EpisodeRepository {
    suspend fun fetchAllEpisodes(): ApiOperation<List<Episode>>
}