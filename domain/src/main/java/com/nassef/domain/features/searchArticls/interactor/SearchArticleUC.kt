package com.nassef.domain.features.searchArticls.interactor

import com.nassef.core.domain.error.ErrorHandler
import com.nassef.core.domain.interactor.BaseUseCase
import com.nassef.domain.entities.ArticlesEntity
import com.nassef.domain.features.searchArticls.model.ArticleSearchRequest
import com.nassef.domain.features.searchArticls.repository.ISearchArticles
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchArticleUC(val repo: ISearchArticles, errorHandler: ErrorHandler) :
    BaseUseCase<ArticlesEntity, ArticleSearchRequest>(
        errorHandler
    ) {
    override fun executeDS(body: ArticleSearchRequest?): Flow<ArticlesEntity> = flow {
        body?.validateRequestContract()
        val result = repo.searchArticle(body!!.remoteMap)
        emit(result)
    }
}