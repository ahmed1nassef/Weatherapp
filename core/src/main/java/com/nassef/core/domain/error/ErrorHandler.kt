package com.nassef.core.domain.error

import com.nassef.core.data.model.exception.AppException

interface ErrorHandler {
    fun getError(throwable: Throwable): AppException

}