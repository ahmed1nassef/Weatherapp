package com.nassef.data.features.getArticles.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nassef.core.domain.model.request.RemoteRequest
import com.nassef.core.domain.repository.remote.INetworkProvider
import com.nassef.data.features.getArticles.repository.remote.ArticlesPagingSource
import com.nassef.domain.entities.ArticlesEntity
import com.nassef.domain.features.getArticles.repository.IArticlesPaginRepository
import kotlinx.coroutines.flow.Flow

class ArticlesPagingRepository(private val provider: INetworkProvider) : IArticlesPaginRepository {
    override fun getPagingArticles(remoteRequest: RemoteRequest):  Flow<PagingData<ArticlesEntity>> {
        val results = Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ), pagingSourceFactory = {
                ArticlesPagingSource(provider , remoteRequest)
            }
        ).flow
        return results
    }
}