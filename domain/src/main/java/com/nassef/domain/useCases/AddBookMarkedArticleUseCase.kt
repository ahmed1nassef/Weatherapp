package com.nassef.domain.useCases

import com.nassef.domain.entities.ArticleX
import com.nassef.domain.repository.IarticlesRepository

class AddBookMarkedArticleUseCase (val repo: IarticlesRepository) {
    suspend operator fun invoke(article: ArticleX) {
        repo.upsertArticle(article)
    }
}