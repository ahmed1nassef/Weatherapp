package com.nassef.domain.features.deleteArticle.interactor

import com.nassef.core.domain.error.ErrorHandler
import com.nassef.core.domain.interactor.BaseUseCase
import com.nassef.domain.features.deleteArticle.reposiptory.IdeleteArticletRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteArticleByIdUC(private val repo: IdeleteArticletRepo, errorHandler: ErrorHandler) :
    BaseUseCase<Boolean, Int>(
        errorHandler
    ) {
    override fun executeDS(body: Int?): Flow<Boolean> = flow {
        repo.deleteArtcileById(body!!)
        emit(true)
    }
}