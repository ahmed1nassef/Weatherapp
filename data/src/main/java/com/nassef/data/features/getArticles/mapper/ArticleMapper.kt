package com.nassef.data.features.getArticles.mapper

import com.nassef.core.data.mapper.Mapper
import com.nassef.data.features.getArticles.model.ArticleDto
import com.nassef.domain.entities.ArticlesHolder

internal object ArticleMapper : Mapper<ArticleDto , ArticlesHolder , Unit>() {
    override fun dtoToDomain(model: ArticleDto): ArticlesHolder {
        return ArticlesHolder(model.articles , model.status , model.totalResults)
    }

}