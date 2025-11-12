package com.nassef.data.features.searchArticls.repository.remote

import com.nassef.core.domain.model.request.RemoteRequest
import com.nassef.data.features.getArticles.model.ArticleDto

interface ISearchRemoteAS {
    suspend fun searchArticle(remoteRequest: RemoteRequest) : ArticleDto
}