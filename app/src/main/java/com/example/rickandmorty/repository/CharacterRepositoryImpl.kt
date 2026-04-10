package com.example.rickandmorty.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.network.ApiOperation
import com.example.network.NetworkDataSource
import com.example.network.data.CharacterPagingSource
import com.example.network.data.NetworkPagingSource
import com.example.network.model.domain.Character
import com.example.network.model.domain.CharacterPage
import com.example.network.model.domain.Episode
import kotlinx.coroutines.flow.Flow

class CharacterRepositoryImpl(
    private val remoteDataSource: NetworkDataSource
) : CharacterRepository {
    override suspend fun getCharacters(): ApiOperation<Character> {
        TODO("Not yet implemented")
    }

    override suspend fun getCharacter(id: Int): ApiOperation<Character> {
        return remoteDataSource.getCharacter(id)
    }

    override suspend fun fetchCharacterPage(
        page: Int,
        params: Map<String, String>
    ): ApiOperation<CharacterPage> {
        return remoteDataSource.getCharacterByPage(page, params)
    }

    override fun getCharactersByName(query: Map<String, String>):
        Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                CharacterPagingSource(
                    remoteDataSource,
                    queryParams = query
                )
//                NetworkPagingSource(
//                    fetchPage = { page -> remoteDataSource.getCharacterByPage(page, query) },
//                    getItems = { it.characters },
//                    getNextUrl = { it.info.next }
//                )
            }
        ).flow
    }

    override suspend fun fetchAllCharactersByName(searchQuery: String): ApiOperation<List<Character>> {
        return remoteDataSource.searchAllCharactersByName(searchQuery)
    }

    override suspend fun getEpisode(episodeId: Int): ApiOperation<Episode> {
        return remoteDataSource.getEpisode(episodeId)
    }

    override suspend fun getEpisodes(episodeIds: List<Int>): ApiOperation<List<Episode>> {
        return remoteDataSource.getEpisodes(episodeIds)
    }
}