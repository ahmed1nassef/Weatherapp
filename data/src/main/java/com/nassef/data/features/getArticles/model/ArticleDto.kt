package com.nassef.data.features.getArticles.model

import com.nassef.core.data.model.dto.BaseDto
import com.nassef.domain.entities.Article

data class ArticleDto(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
) : BaseDto()
