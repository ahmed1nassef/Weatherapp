package com.nassef.domain.features.searchArticls.model

import com.nassef.core.domain.model.request.IRequestValidation
import com.nassef.core.domain.model.request.RemoteRequest
import com.nassef.core.domain.model.request.RequestContractType
import com.nassef.domain.appConstants.SEARCH_QUERY_KEY

data class ArticleSearchRequest(val searchQuery: String) : IRequestValidation {
    override fun isInitialState(): Boolean {
        return searchQuery.isEmpty()
    }

    override val remoteMap: RemoteRequest
        get() = RemoteRequest(
            requestQueries = hashMapOf(SEARCH_QUERY_KEY to searchQuery)
        )

    override fun getRequestContracts(): HashMap<RequestContractType, HashMap<String, Boolean>> {
        return hashMapOf(RequestContractType.QUERIES to hashMapOf(SEARCH_QUERY_KEY to true))
    }
}