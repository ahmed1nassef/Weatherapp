package com.nassef.domain.utilities

sealed class Results<T> {
    data class Success<T>(val data: T) : Results<T>()
    data class Error<T>(val error: Exception?, val errorMsg: String?, val errorCode: Int?) :
        Results<T>()

    object Loading : Results<Nothing>()
}