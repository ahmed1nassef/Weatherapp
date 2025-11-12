package com.nassef.domain.useCases

import com.nassef.domain.entities.Article
import com.nassef.domain.repository.IarticlesRepository

class AddBookMarkedArticleUseCase (val repo: IarticlesRepository) {
    suspend operator fun invoke(article: Article) {
        repo.upsertArticle(article)
    }
}