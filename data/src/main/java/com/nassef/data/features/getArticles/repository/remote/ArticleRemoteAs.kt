package com.nassef.data.features.getArticles.repository.remote

import com.nassef.core.domain.model.request.RemoteRequest
import com.nassef.core.domain.repository.remote.INetworkProvider
import com.nassef.data.features.getArticles.model.ArticleDto
import com.nassef.data.network.ArticlesApi

class ArticleRemoteAs(private val provider: INetworkProvider) : IArticlesRemoteAS {
    override suspend fun getAllArticles(remoteRequest: RemoteRequest): ArticleDto {
        return provider.get(
            responseWrappedModel = ArticleDto::class.java,
            pathUrl = Articles_PATH,
            headers = remoteRequest.requestHeaders,
            queryParams = remoteRequest.requestQueries
        )
    }

    companion object {
        private const val Articles_PATH = "top-headlines"
    }

}