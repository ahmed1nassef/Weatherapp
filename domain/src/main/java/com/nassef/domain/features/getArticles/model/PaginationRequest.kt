package com.nassef.domain.features.getArticles.model

import com.nassef.core.domain.model.request.IRequestValidation
import com.nassef.core.domain.model.request.RemoteRequest
import com.nassef.core.domain.model.request.RequestContractType
import com.nassef.domain.appConstants.COUNTRY_KEY
import com.nassef.domain.appConstants.PAGE_KEY

data class PaginationRequest(
    val page: Int,
    val pageSize: Int = 20,
    val country: String = "us"
) : IRequestValidation {

    override fun isInitialState(): Boolean = false

    override val remoteMap: RemoteRequest
        get() = RemoteRequest(
            requestQueries = hashMapOf(
                COUNTRY_KEY to country,
                PAGE_KEY to page,
                "pageSize" to pageSize
            )
        )

    override fun getRequestContracts(): HashMap<RequestContractType, HashMap<String, Boolean>> {
        return hashMapOf(
            RequestContractType.QUERIES to hashMapOf(
                COUNTRY_KEY to true,
                PAGE_KEY to true
            )
        )
    }
}
