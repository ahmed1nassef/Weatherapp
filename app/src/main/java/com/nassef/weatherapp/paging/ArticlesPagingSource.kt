package com.nassef.weatherapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nassef.core.data.model.Resource
import com.nassef.domain.entities.Article
import com.nassef.domain.features.getArticles.interactor.GetPaginatedArticlesUC
import com.nassef.domain.features.getArticles.model.PaginationRequest
import com.nassef.domain.model.PaginatedResult
import kotlinx.coroutines.CoroutineScope

class ArticlesPagingSource(
    private val useCase: GetPaginatedArticlesUC,
    private val country: String,
    private val scope: CoroutineScope
) : PagingSource<Int, Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: 1

        return try {
            var result: PaginatedResult<Article>? = null
            var errorMessage: String? = null

            // Call domain use case - gets Resource with loading/error handling!
            useCase.invoke(
                scope = scope,
                body = PaginationRequest(
                    page = page,
                    pageSize = params.loadSize,
                    country = country
                )
            ) { resource ->
                when (resource) {
                    is Resource.Success -> {
                        result = resource.model
                    }
                    is Resource.Failure -> {
                        errorMessage = resource.exception.message
                    }
                    is Resource.Progress -> {
                        // Loading state handled by Paging 3
                    }
                }
            }.join()

            if (errorMessage != null) {
                LoadResult.Error(Exception(errorMessage))
            } else if (result != null) {
                LoadResult.Page(
                    data = result!!.data,
                    prevKey = if (result!!.hasPreviousPage) page - 1 else null,
                    nextKey = if (result!!.hasNextPage) page + 1 else null
                )
            } else {
                LoadResult.Error(Exception("Unknown error occurred"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
