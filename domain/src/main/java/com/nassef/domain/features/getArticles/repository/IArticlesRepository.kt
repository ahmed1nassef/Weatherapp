package com.nassef.domain.features.getArticles.repository

import com.nassef.core.domain.model.request.RemoteRequest
import com.nassef.domain.entities.ArticlesEntity

interface IArticlesRepository {
    suspend fun getAllArticles(remoteRequest: RemoteRequest): ArticlesEntity
}