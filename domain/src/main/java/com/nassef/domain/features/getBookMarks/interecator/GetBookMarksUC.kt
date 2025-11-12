package com.nassef.domain.features.getBookMarks.interecator

import com.nassef.core.domain.error.ErrorHandler
import com.nassef.core.domain.interactor.BaseUseCase
import com.nassef.domain.entities.Article
import com.nassef.domain.features.getBookMarks.repository.IbookMarksRepo
import kotlinx.coroutines.flow.Flow

class GetBookMarksUC(private val repo: IbookMarksRepo, errorHandler: ErrorHandler) : BaseUseCase<List<Article> , Unit>(
    errorHandler
) {
    override fun executeDS(body: Unit?): Flow<List<Article>> = repo.getBookMarks()
}