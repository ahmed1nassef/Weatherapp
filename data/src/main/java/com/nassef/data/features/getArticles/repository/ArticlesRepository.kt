package com.nassef.data.features.getArticles.repository

import com.nassef.core.domain.model.request.RemoteRequest
import com.nassef.data.features.getArticles.mapper.ArticleMapper
import com.nassef.data.features.getArticles.repository.remote.IArticlesRemoteAS
import com.nassef.domain.entities.ArticlesEntity
import com.nassef.domain.features.getArticles.repository.IArticlesRepository

class ArticlesRepository (private val remoteAS: IArticlesRemoteAS) : IArticlesRepository {
    override suspend fun getAllArticles(remoteRequest: RemoteRequest): ArticlesEntity {
        val result = remoteAS.getAllArticles(remoteRequest)
        return ArticleMapper.dtoToDomain(result)
    }
}