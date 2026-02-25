package com.nassef.data.features.getArticles.repository

import com.nassef.core.domain.model.request.RemoteRequest
import com.nassef.data.features.getArticles.mapper.ArticleMapper
import com.nassef.data.features.getArticles.repository.remote.IArticlesRemoteAS
import com.nassef.domain.entities.Article
import com.nassef.domain.entities.ArticlesEntity
import com.nassef.domain.features.getArticles.repository.IArticlesRepository
import com.nassef.domain.model.PaginatedResult

class ArticlesRepository(private val remoteAS: IArticlesRemoteAS) : IArticlesRepository {
    override suspend fun getAllArticles(remoteRequest: RemoteRequest): ArticlesEntity {
        val result = remoteAS.getAllArticles(remoteRequest)
        return ArticleMapper.dtoToDomain(result)
    }

    override suspend fun getArticlesPage(
        page: Int,
        pageSize: Int,
        country: String
    ): PaginatedResult<Article> {
        val remoteRequest = RemoteRequest(
            requestQueries = hashMapOf(
                "country" to country,
                "page" to page.toString(),
                "pageSize" to pageSize.toString()
            )
        )

        val result = remoteAS.getAllArticles(remoteRequest)
        val articlesEntity = ArticleMapper.dtoToDomain(result)

        return PaginatedResult(
            data = articlesEntity.articles,
            page = page,
            pageSize = pageSize,
            totalResults = articlesEntity.totalResults,
            hasNextPage = articlesEntity.articles.size == pageSize,
            hasPreviousPage = page > 1
        )
    }
}