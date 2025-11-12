package com.nassef.domain.features.getArticles.repository

import com.nassef.core.domain.model.request.RemoteRequest
import com.nassef.domain.entities.ArticlesHolder

interface IArticlesRepository {
    suspend fun getAllArticles(remoteRequest: RemoteRequest): ArticlesHolder
}