package com.nassef.domain.features.getBookMarks.repository

import com.nassef.domain.entities.Article
import kotlinx.coroutines.flow.Flow

interface IbookMarksRepo {
    fun getBookMarks(): Flow<List<Article>>
}