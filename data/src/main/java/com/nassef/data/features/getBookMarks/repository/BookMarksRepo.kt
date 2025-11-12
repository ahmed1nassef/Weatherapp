package com.nassef.data.features.getBookMarks.repository

import com.nassef.data.local.ArticleDao
import com.nassef.domain.entities.Article
import com.nassef.domain.features.getBookMarks.repository.IbookMarksRepo
import kotlinx.coroutines.flow.Flow

class BookMarksRepo(private val articleDao: ArticleDao) : IbookMarksRepo {
    override fun getBookMarks(): Flow<List<Article>> {
        return articleDao.getAllArticles()
    }
}