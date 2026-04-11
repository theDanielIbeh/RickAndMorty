package com.example.rickandmorty.core.network.ktor

import com.example.rickandmorty.core.network.ApiOperation
import com.example.rickandmorty.core.network.NetworkDataSource
import com.example.rickandmorty.core.network.model.domain.Character
import com.example.rickandmorty.core.network.model.domain.CharacterPage
import com.example.rickandmorty.core.network.model.domain.Episode
import com.example.rickandmorty.core.network.model.domain.EpisodePage
import com.example.rickandmorty.core.network.model.remote.RemoteCharacter
import com.example.rickandmorty.core.network.model.remote.RemoteCharacterPage
import com.example.rickandmorty.core.network.model.remote.RemoteEpisode
import com.example.rickandmorty.core.network.model.remote.RemoteEpisodePage
import com.example.rickandmorty.core.network.model.remote.toDomainCharacter
import com.example.rickandmorty.core.network.model.remote.toDomainCharacterPage
import com.example.rickandmorty.core.network.model.remote.toDomainEpisode
import com.example.rickandmorty.core.network.model.remote.toDomainEpisodePage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class KtorNetworkDataSource : NetworkDataSource {
    internal val client = HttpClient(OkHttp) {
        defaultRequest {
            url("https://rickandmortyapi.com/api/")
        }

        install(Logging.Companion) {
            logger = Logger.Companion.SIMPLE
        }

        install(ContentNegotiation.Plugin) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }

        expectSuccess = true
    }

    private var characterCache = mutableMapOf<Int, Character>()

    override suspend fun getCharacter(id: Int): ApiOperation<Character> {
        characterCache[id]?.let { return ApiOperation.Success(it) }
        return safeApiCall {
            client.get("character/$id")
                .body<RemoteCharacter>()
                .toDomainCharacter()
                .also { characterCache[id] = it }
        }
    }

    override suspend fun getCharacterByPage(
        pageNumber: Int,
        queryParams: Map<String, String>
    ): ApiOperation<CharacterPage> {
        return safeApiCall {
            client.get("character") {
                url {
                    parameters.append("page", pageNumber.toString())
                    queryParams.forEach { parameters.append(it.key, it.value) }
                }
            }
                .body<RemoteCharacterPage>()
                .toDomainCharacterPage()
        }
    }

    override suspend fun searchAllCharactersByName(searchQuery: String): ApiOperation<List<Character>> {
        val data = mutableListOf<Character>()
        var exception: Exception? = null

        getCharacterByPage(
            pageNumber = 1,
            queryParams = mapOf("name" to searchQuery)
        ).onSuccess { firstPage ->
            val totalPageCount = firstPage.info.pages
            data.addAll(firstPage.characters)

            repeat(totalPageCount - 1) { index ->
                getCharacterByPage(
                    pageNumber = index + 2,
                    queryParams = mapOf("name" to searchQuery)
                ).onSuccess { nextPage ->
                    data.addAll(nextPage.characters)
                }.onFailure { error ->
                    exception = error
                }

                if (exception != null) {
                    return@onSuccess
                }
            }
        }.onFailure {
            exception = it
        }

        return exception?.let { ApiOperation.Failure(it) } ?: ApiOperation.Success(data)
    }


    override suspend fun getEpisode(episodeId: Int): ApiOperation<Episode> {
        return safeApiCall {
            client.get("episode/$episodeId")
                .body<RemoteEpisode>()
                .toDomainEpisode()
        }
    }

    override suspend fun getEpisodes(episodeIds: List<Int>): ApiOperation<List<Episode>> {
        return if (episodeIds.size == 1) {
            getEpisode(episodeIds[0]).mapSuccess {
                listOf(it)
            }
        } else {
            val idsCommaSeparated = episodeIds.joinToString(separator = ",")
            safeApiCall {
                client.get("episode/$idsCommaSeparated")
                    .body<List<RemoteEpisode>>()
                    .map { it.toDomainEpisode() }
            }
        }
    }

    override suspend fun getEpisodesByPage(pageIndex: Int): ApiOperation<EpisodePage> {
        return safeApiCall {
            client.get("episode") {
                url {
                    parameters.append("page", pageIndex.toString())
                }
            }
                .body<RemoteEpisodePage>()
                .toDomainEpisodePage()
        }
    }

    override suspend fun getAllEpisodes(): ApiOperation<List<Episode>> {
        val data = mutableListOf<Episode>()
        var exception: Exception? = null

        getEpisodesByPage(pageIndex = 1).onSuccess { firstPage ->
            val totalPageCount = firstPage.info.pages
            data.addAll(firstPage.episodes)

            repeat(totalPageCount - 1) { index ->
                getEpisodesByPage(pageIndex = index + 2).onSuccess { nextPage ->
                    data.addAll(nextPage.episodes)
                }.onFailure { error ->
                    exception = error
                }

                if (exception != null) {
                    return@onSuccess
                }
            }
        }.onFailure {
            exception = it
        }

        return exception?.let { ApiOperation.Failure(it) } ?: ApiOperation.Success(data)
    }

    private inline fun <T> safeApiCall(apiCall: () -> T): ApiOperation<T> {
        return try {
            ApiOperation.Success(data = apiCall())
        } catch (e: Exception) {
            ApiOperation.Failure(exception = e)
        }
    }
}