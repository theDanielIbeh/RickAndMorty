package com.example.rickandmorty.core.data.repository

import androidx.paging.PagingData
import com.example.rickandmorty.core.network.ApiOperation
import com.example.rickandmorty.core.network.model.domain.Character
import com.example.rickandmorty.core.network.model.domain.CharacterPage
import com.example.rickandmorty.core.network.model.domain.Episode
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    suspend fun getCharacters(): ApiOperation<Character>
    suspend fun getCharacter(id: Int): ApiOperation<Character>
    suspend fun fetchCharacterPage(page: Int, params: Map<String, String> = emptyMap()): ApiOperation<CharacterPage>
    fun getCharactersByName(query: Map<String, String> = emptyMap()): Flow<PagingData<Character>>
    suspend fun fetchAllCharactersByName(searchQuery: String): ApiOperation<List<Character>>
    suspend fun getEpisode(episodeId: Int): ApiOperation<Episode>
    suspend fun getEpisodes(episodeIds: List<Int>): ApiOperation<List<Episode>>
}
