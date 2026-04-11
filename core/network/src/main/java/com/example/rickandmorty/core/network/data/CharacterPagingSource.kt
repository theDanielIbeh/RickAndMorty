package com.example.rickandmorty.core.network.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickandmorty.core.network.NetworkDataSource
import com.example.rickandmorty.core.network.model.domain.Character

class CharacterPagingSource(
    val networkDataSource: NetworkDataSource,
    val queryParams: Map<String, String>
) : PagingSource<Int, Character>() {
    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, Character> {
        var data = LoadResult.Page<Int, Character>(
            data = emptyList(),
            prevKey = null,
            nextKey = null
        )
        var exception: Exception? = null
        // Start refresh at page 1 if undefined.
        val nextPageNumber = params.key ?: 1
        val response =
            networkDataSource.getCharacterByPage(nextPageNumber, queryParams)
        response.onSuccess {
            data = LoadResult.Page(
                data = it.characters,
                prevKey = null, // Only paging forward.
                nextKey = it.info.next?.split("=")?.last()?.toInt()
            )
        }.onFailure {
            exception = it

        }
        return exception?.let { LoadResult.Error(it) } ?: data
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        // Try to find the page key of the closest page to anchorPosition from
        // either the prevKey or the nextKey; you need to handle nullability
        // here.
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey are null -> anchorPage is the
        //    initial page, so return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}