package com.example.network

import com.example.network.model.domain.Character
import com.example.network.model.domain.CharacterPage
import com.example.network.model.domain.Episode
import com.example.network.model.domain.EpisodePage

interface NetworkDataSource {
    suspend fun getCharacter(id: Int): ApiOperation<Character>
    suspend fun getCharacterByPage(
        pageNumber: Int,
        queryParams: Map<String, String>
    ): ApiOperation<CharacterPage>
    suspend fun searchAllCharactersByName(searchQuery: String): ApiOperation<List<Character>>
    suspend fun getEpisode(episodeId: Int): ApiOperation<Episode>
    suspend fun getEpisodes(episodeIds: List<Int>): ApiOperation<List<Episode>>
    suspend fun getEpisodesByPage(pageIndex: Int): ApiOperation<EpisodePage>
    suspend fun getAllEpisodes(): ApiOperation<List<Episode>>
}

sealed interface ApiOperation<T> {
    data class Success<T>(val data: T) : ApiOperation<T>
    data class Failure<T>(val exception: Exception) : ApiOperation<T>

    fun <R> mapSuccess(transform: (T) -> R): ApiOperation<R> {
        return when (this) {
            is Success -> Success(transform(data))
            is Failure -> Failure(exception)
        }
    }

    suspend fun onSuccess(block: suspend (T) -> Unit): ApiOperation<T> {
        if (this is Success) block(data)
        return this
    }

    fun onFailure(block: (Exception) -> Unit): ApiOperation<T> {
        if (this is Failure) block(exception)
        return this
    }
}