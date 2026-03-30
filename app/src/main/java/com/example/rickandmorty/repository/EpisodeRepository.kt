package com.example.rickandmorty.repository

import com.example.network.ApiOperation
import com.example.network.model.domain.Episode

interface EpisodeRepository {
    suspend fun fetchAllEpisodes(): ApiOperation<List<Episode>>
}