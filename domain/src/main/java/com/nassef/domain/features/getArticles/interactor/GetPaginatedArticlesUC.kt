package com.nassef.domain.features.getArticles.interactor

import com.nassef.core.domain.error.ErrorHandler
import com.nassef.core.domain.interactor.BaseUseCase
import com.nassef.domain.entities.Article
import com.nassef.domain.features.getArticles.model.PaginationRequest
import com.nassef.domain.features.getArticles.repository.IArticlesRepository
import com.nassef.domain.model.PaginatedResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetPaginatedArticlesUC(
    private val repo: IArticlesRepository,
    errorHandler: ErrorHandler
) : BaseUseCase<PaginatedResult<Article>, PaginationRequest>(errorHandler) {

    override fun executeDS(body: PaginationRequest?): Flow<PaginatedResult<Article>> {
        return flow {
            requireBody(body).validateRequestContract()

            val result = repo.getArticlesPage(
                page = body!!.page,
                pageSize = body.pageSize,
                country = body.country
            )

            emit(result)
        }
    }
}
