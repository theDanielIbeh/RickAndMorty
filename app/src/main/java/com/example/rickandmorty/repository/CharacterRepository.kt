package com.example.rickandmorty.repository

import com.example.network.ApiOperation
import com.example.network.model.domain.Character
import com.example.network.model.domain.CharacterPage
import com.example.network.model.domain.Episode

interface CharacterRepository {
    suspend fun getCharacters(): ApiOperation<Character>
    suspend fun getCharacter(id: Int): ApiOperation<Character>
    suspend fun fetchCharacterPage(page: Int, params: Map<String, String> = emptyMap()):
        ApiOperation<CharacterPage>
    suspend fun getEpisode(episodeId: Int): ApiOperation<Episode>
    suspend fun getEpisodes(episodeIds: List<Int>): ApiOperation<List<Episode>>
}
