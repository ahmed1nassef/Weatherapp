package com.nassef.domain.features.getArticles.interactor

import com.nassef.core.domain.error.ErrorHandler
import com.nassef.core.domain.interactor.BaseUseCase
import com.nassef.domain.entities.ArticlesHolder
import com.nassef.domain.features.getArticles.model.ArticleRequest
import com.nassef.domain.features.getArticles.repository.IArticlesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetArticlesUC(private val repo: IArticlesRepository, errorHandler: ErrorHandler) :
    BaseUseCase<ArticlesHolder, ArticleRequest>(errorHandler) {

    override fun executeDS(body: ArticleRequest?): Flow<ArticlesHolder> {
        return flow {
            requireBody(body).validateRequestContract()
            val articles = repo.getAllArticles(body!!.remoteMap)
            emit(articles)
        }
    }
}