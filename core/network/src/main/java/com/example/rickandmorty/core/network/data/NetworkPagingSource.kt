package com.example.rickandmorty.core.network.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickandmorty.core.network.ApiOperation

class NetworkPagingSource<T : Any, V : Any>(
    private val fetchPage: suspend (page: Int) -> ApiOperation<V>,
    private val getItems: (V) -> List<T>,
    private val getNextUrl: (V) -> String?,
    private val getPrevUrl: (V) -> String? = { null }
) : PagingSource<Int, T>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        var data = LoadResult.Page<Int, T>(
            data = emptyList(),
            prevKey = null,
            nextKey = null
        )
        var exception: Exception? = null
        val pageNumber = params.key ?: 1
        fetchPage(pageNumber)
            .onSuccess {
                data = LoadResult.Page(
                    data = getItems(it),
                    prevKey = getPrevUrl(it)?.split("=")?.last()?.toInt(),
                    nextKey = getNextUrl(it)?.split("=")?.last()?.toInt()
                )
            }
            .onFailure { exception = it }
        return exception?.let { LoadResult.Error(it) } ?: data
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}