package com.nassef.domain.features.getArticles.interactor

import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.nassef.core.domain.error.ErrorHandler
import com.nassef.core.domain.interactor.BaseUseCase
import com.nassef.domain.entities.Article
import com.nassef.domain.entities.ArticlesEntity
import com.nassef.domain.features.getArticles.model.ArticleRequest
import com.nassef.domain.features.getArticles.repository.IArticlesPaginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetPagingArticlesUC(private val repo: IArticlesPaginRepository)  {
    operator fun invoke(body: ArticleRequest?): Flow<PagingData<ArticlesEntity>> {
        return repo.getPagingArticles(body!!.remoteMap)
    }
}