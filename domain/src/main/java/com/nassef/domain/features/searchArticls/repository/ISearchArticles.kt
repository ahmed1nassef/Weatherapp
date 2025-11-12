package com.nassef.domain.features.searchArticls.repository

import com.nassef.core.domain.model.request.RemoteRequest
import com.nassef.domain.entities.ArticlesEntity

interface ISearchArticles {
    suspend fun searchArticle(remoteRequest: RemoteRequest) : ArticlesEntity
}