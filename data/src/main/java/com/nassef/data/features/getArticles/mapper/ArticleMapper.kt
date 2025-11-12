package com.nassef.data.features.getArticles.mapper

import com.nassef.core.data.mapper.Mapper
import com.nassef.data.features.getArticles.model.ArticleDto
import com.nassef.domain.entities.ArticlesEntity

internal object ArticleMapper : Mapper<ArticleDto , ArticlesEntity , Unit>() {
    override fun dtoToDomain(model: ArticleDto): ArticlesEntity {
        return ArticlesEntity(model.articles , model.status , model.totalResults)
    }

}