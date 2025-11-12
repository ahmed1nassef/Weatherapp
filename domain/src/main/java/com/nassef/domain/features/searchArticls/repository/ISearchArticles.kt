package com.nassef.domain.features.searchArticls.repository

import com.nassef.core.domain.model.request.RemoteRequest
import com.nassef.domain.entities.ArticlesHolder

interface ISearchArticles {
    suspend fun searchArticle(remoteRequest: RemoteRequest) : ArticlesHolder
}