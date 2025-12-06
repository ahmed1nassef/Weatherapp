package com.nassef.domain.features.getArticles.repository

import androidx.paging.PagingData
import com.nassef.core.domain.model.request.RemoteRequest
import com.nassef.domain.entities.ArticlesEntity
import kotlinx.coroutines.flow.Flow

interface IArticlesPaginRepository {
    fun getPagingArticles(remoteRequest: RemoteRequest):  Flow<PagingData<ArticlesEntity>>

}