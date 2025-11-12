package com.nassef.domain.features.saveArticle.repository

import com.nassef.domain.entities.Article

interface ISaveArticleRepo {
    suspend fun saveArticle(article: Article)
}