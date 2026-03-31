package com.example.rickandmorty.repository

import com.example.network.ApiOperation
import com.example.network.NetworkDataSource
import com.example.network.model.domain.Character
import com.example.network.model.domain.CharacterPage
import com.example.network.model.domain.Episode

class CharacterRepositoryImpl(
    private val remoteDataSource: NetworkDataSource
) : CharacterRepository {
    override suspend fun getCharacters(): ApiOperation<Character> {
        TODO("Not yet implemented")
    }

    override suspend fun getCharacter(id: Int): ApiOperation<Character> {
        return remoteDataSource.getCharacter(id)
    }

    override suspend fun fetchCharacterPage(page: Int, params: Map<String, String>): ApiOperation<CharacterPage> {
        return remoteDataSource.getCharacterByPage(page, params)
    }

    override suspend fun fetchAllCharactersByName(searchQuery: String): ApiOperation<List<Character>> {
        return remoteDataSource.searchAllCharactersByName(searchQuery)
    }

    override suspend fun getEpisode(episodeId: Int): ApiOperation<Episode>  {
        return remoteDataSource.getEpisode(episodeId)
    }

    override suspend fun getEpisodes(episodeIds: List<Int>): ApiOperation<List<Episode>> {
        return remoteDataSource.getEpisodes(episodeIds)
        }
    }