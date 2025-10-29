package com.nassef.domain.useCases

import com.nassef.domain.entities.ArticleX
import com.nassef.domain.repository.IarticlesRepository
import com.nassef.domain.utilities.Results

class GetAllArticlesUseCase(private val articleRepository: IarticlesRepository,/* val country: String*/) {
    operator suspend fun invoke(country: String): Results<List<ArticleX>> {
        val isValidCountryCode = validateCountryCode(country)
        if (isValidCountryCode.not())
            return Results.Error(null , "error in country code" , null)
        val results =  articleRepository.getAllArticles(country = country)
//        if(results is Results.Success<List<ArticleX>>){
//            results.data.map {
//                it.description =
//            }
//        }
        return results
    }
    private fun validateCountryCode(country: String) : Boolean{
        var isValid = true ;
        if(country.isEmpty() || country.length < 1)
            isValid = false
        return isValid
    }
}