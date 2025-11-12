package com.nassef.domain.features.saveArticle.interactor

import com.nassef.core.domain.error.ErrorHandler
import com.nassef.core.domain.interactor.BaseUseCase
import com.nassef.domain.entities.Article
import com.nassef.domain.features.saveArticle.repository.ISaveArticleRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SaveArticleUC(val repo: ISaveArticleRepo, errorHandler: ErrorHandler) : BaseUseCase<String , Article>(
    errorHandler
) {
    override fun executeDS(body: Article?): Flow<String> = flow {
        repo.saveArticle(body!!)
        emit("article is saved successfully")
    }

}