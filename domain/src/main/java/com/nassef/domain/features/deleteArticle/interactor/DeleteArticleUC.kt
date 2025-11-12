package com.nassef.domain.features.deleteArticle.interactor

import com.nassef.core.domain.error.ErrorHandler
import com.nassef.core.domain.interactor.BaseUseCase
import com.nassef.domain.entities.Article
import com.nassef.domain.features.deleteArticle.reposiptory.IdeleteArticletRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteArticleUC(private val repo: IdeleteArticletRepo, errorHandler: ErrorHandler) :
    BaseUseCase<Unit, Article>(
        errorHandler
    ) {
    override fun executeDS(body: Article?): Flow<Unit> = flow {
        repo.deleteArtcile(body!!)
    }
}