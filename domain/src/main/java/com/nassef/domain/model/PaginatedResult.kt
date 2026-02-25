package com.nassef.domain.model

data class PaginatedResult<T>(
    val data: List<T>,
    val page: Int,
    val pageSize: Int,
    val totalResults: Int? = null,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean
)
