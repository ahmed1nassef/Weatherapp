package com.nassef.domain.features.splashHistory.interactor

import com.nassef.core.domain.error.ErrorHandler
import com.nassef.core.domain.interactor.BaseUseCase
import com.nassef.domain.features.splashHistory.repository.IStartDestinationRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetStartDestinationUC (private val repo : IStartDestinationRepo, errorHandler: ErrorHandler): BaseUseCase<Boolean , Boolean>(
    errorHandler
) {
    override fun executeDS(body: Boolean?): Flow<Boolean> = flow {
        val isOpened = repo.isSplashOpenedBefore()
        if(repo.isSplashOpenedBefore().not()) {
            repo.saveSplashHistory(body!!)
        }
        emit(isOpened)
    }
}