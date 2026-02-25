package com.nassef.domain.features.getArticles.repository

import com.nassef.core.domain.model.request.RemoteRequest
import com.nassef.domain.entities.Article
import com.nassef.domain.entities.ArticlesEntity
import com.nassef.domain.model.PaginatedResult

interface IArticlesRepository {
    suspend fun getAllArticles(remoteRequest: RemoteRequest): ArticlesEntity

    suspend fun getArticlesPage(
        page: Int,
        pageSize: Int,
        country: String
    ): PaginatedResult<Article>
}