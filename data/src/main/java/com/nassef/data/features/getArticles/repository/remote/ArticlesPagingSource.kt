package com.nassef.data.features.getArticles.repository.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nassef.core.domain.model.request.RemoteRequest
import com.nassef.core.domain.repository.remote.INetworkProvider
import com.nassef.data.features.getArticles.mapper.ArticleMapper
import com.nassef.data.features.getArticles.model.ArticleDto
import com.nassef.domain.appConstants.PAGE_KEY
import com.nassef.domain.entities.ArticlesEntity

class ArticlesPagingSource(
    private val provider: INetworkProvider,
    private val remoteRequest: RemoteRequest
) : PagingSource<Int, ArticlesEntity>() {
    private val Articles_PATH = "top-headlines"

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticlesEntity> {
        val pageIndex = params.key ?: 1
        val pageSize = params.loadSize
        val queryParams = remoteRequest.requestQueries
        queryParams[PAGE_KEY] = pageIndex

        Log.i("Paging", "" + pageIndex)
        return try {
//            val responseData = apiService.fetchImages(pageIndex, pageSize)
            val responseData = provider.get<ArticleDto>(
                responseWrappedModel = ArticleDto::class.java,
                pathUrl = Articles_PATH,
                headers = remoteRequest.requestHeaders,
                queryParams = queryParams
            )


            LoadResult.Page(
                data = listOf(ArticleMapper.dtoToDomain(responseData)),
                prevKey = if (pageIndex == 1) null else pageIndex - 1,
//                nextKey = if (responseData.body()!!.isEmpty()) null else pageIndex + 1
                nextKey = if (responseData == null) null else pageIndex + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    //    override fun getRefreshKey(state: PagingState<Int, ArticleDto>): Int? {
//        return state.anchorPosition?.let { anchor ->
//            state.closestPageToPosition(anchor)?.prevKey?.plus(numOfOffScreenPage)
//                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(numOfOffScreenPage)
//
//        }
//    }
    override fun getRefreshKey(state: PagingState<Int, ArticlesEntity>): Int? {
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