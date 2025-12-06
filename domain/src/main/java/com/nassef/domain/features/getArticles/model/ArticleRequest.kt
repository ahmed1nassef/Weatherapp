package com.nassef.domain.features.getArticles.model

import com.nassef.core.domain.model.request.IRequestValidation
import com.nassef.core.domain.model.request.RemoteRequest
import com.nassef.core.domain.model.request.RequestContractType
import com.nassef.domain.appConstants.COUNTRY_KEY
import com.nassef.domain.appConstants.PAGE_KEY

data class ArticleRequest(var countryCode: String , var pageNum: Int = 1) : IRequestValidation {
    override fun isInitialState(): Boolean {
        return countryCode.isEmpty()
    }

    override val remoteMap: RemoteRequest
        get() = RemoteRequest(
            requestQueries = hashMapOf(COUNTRY_KEY to countryCode , PAGE_KEY to pageNum)
        )

    override fun getRequestContracts(): HashMap<RequestContractType, HashMap<String, Boolean>> {
        return hashMapOf(
            RequestContractType.QUERIES to hashMapOf(COUNTRY_KEY to true, PAGE_KEY to true)
        )
    }

}