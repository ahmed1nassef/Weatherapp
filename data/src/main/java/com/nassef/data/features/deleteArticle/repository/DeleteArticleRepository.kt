package com.nassef.data.features.deleteArticle.repository

import com.nassef.data.local.ArticleDao
import com.nassef.data.local.ArticleEntityMapper
import com.nassef.domain.entities.Article
import com.nassef.domain.features.deleteArticle.reposiptory.IdeleteArticletRepo

class DeleteArticleRepository(private val dao: ArticleDao) : IdeleteArticletRepo {
    override suspend fun deleteArtcileById(id: Int) {
        dao.deleteArticleById(id)
    }

    override suspend fun deleteArtcile(article: Article) {
        val entity = ArticleEntityMapper.toEntity(article)
        dao.deleteArticle(entity)
    }
}