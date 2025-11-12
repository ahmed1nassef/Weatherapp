package com.nassef.data.features.searchArticls.repository.remote

import com.nassef.core.domain.model.request.RemoteRequest
import com.nassef.core.domain.repository.remote.INetworkProvider
import com.nassef.data.features.getArticles.model.ArticleDto

class SearchRemoteAS(private val provider: INetworkProvider) : ISearchRemoteAS {
    override suspend fun searchArticle(remoteRequest: RemoteRequest): ArticleDto {
        return provider.get(
            ArticleDto::class.java,
            SEARCH_ARTICLES_PATH,
            remoteRequest.requestHeaders,
            remoteRequest.requestQueries
        )
    }

    companion object {
        const val SEARCH_ARTICLES_PATH = "everything"
    }
}