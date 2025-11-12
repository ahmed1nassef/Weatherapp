package com.nassef.data.features.searchArticls.repository

import com.nassef.core.domain.model.request.RemoteRequest
import com.nassef.data.features.getArticles.mapper.ArticleMapper
import com.nassef.data.features.searchArticls.repository.remote.ISearchRemoteAS
import com.nassef.domain.entities.ArticlesEntity
import com.nassef.domain.features.searchArticls.repository.ISearchArticles

class SearchArticlesRepository(private val seachArticle: ISearchRemoteAS) : ISearchArticles {
    override suspend fun searchArticle(remoteRequest: RemoteRequest): ArticlesEntity {
        val result = seachArticle.searchArticle(remoteRequest)
        return ArticleMapper.dtoToDomain(result)
    }
}