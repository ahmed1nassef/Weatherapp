package com.nassef.data.features.getArticles.repository.remote

import com.nassef.core.domain.model.request.RemoteRequest
import com.nassef.data.features.getArticles.model.ArticleDto

interface IArticlesRemoteAS {
    suspend fun getAllArticles(remoteRequest: RemoteRequest) : ArticleDto
}
//AS stands for App Service