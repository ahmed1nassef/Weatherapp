package com.nassef.domain.useCases

import android.text.TextUtils
import androidx.core.text.isDigitsOnly
import com.nassef.domain.entities.ArticleX
import com.nassef.domain.repository.IarticlesRepository
import com.nassef.domain.utilities.Results

class SearchForArticleUseCase(private val articleRepository: IarticlesRepository/* val country: String*/) {
    operator suspend fun invoke(searchQuery: String): Results<List<ArticleX>> {
        val isValidSearchQuery = validateSearchQuery(searchQuery)
        if (isValidSearchQuery.not())
            return Results.Error(null, "error search query can't be empty", null)
        return articleRepository.getArticleByKeyWord(searchTxt = searchQuery)
    }

    private fun validateSearchQuery(searchQuery: String): Boolean {
        var isValid = true;
        if (searchQuery.isEmpty())
            isValid = false
        return isValid
    }
}