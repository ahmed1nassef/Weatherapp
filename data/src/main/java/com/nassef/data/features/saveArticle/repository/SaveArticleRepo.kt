package com.nassef.data.features.saveArticle.repository

import com.nassef.data.local.ArticleDao
import com.nassef.data.local.ArticleEntityMapper
import com.nassef.domain.entities.Article
import com.nassef.domain.features.saveArticle.repository.ISaveArticleRepo

class SaveArticleRepo(private val articleDao: ArticleDao) : ISaveArticleRepo {
    override suspend fun saveArticle(article: Article) {
        val entity = ArticleEntityMapper.toEntity(article)
        articleDao.upsertArticle(entity)
    }
}