package com.nassef.data.local

import com.nassef.domain.entities.Article

object ArticleEntityMapper {

    fun toEntity(article: Article): ArticleEntity {
        return ArticleEntity(
            id = article.id,
            author = article.author,
            content = article.content,
            description = article.description,
            publishedAt = article.publishedAt,
            source = article.source,
            title = article.title,
            url = article.url,
            urlToImage = article.urlToImage,
            isBookMarked = article.isBookMarked
        )
    }

    fun toDomain(entity: ArticleEntity): Article {
        return Article(
            id = entity.id,
            author = entity.author,
            content = entity.content,
            description = entity.description,
            publishedAt = entity.publishedAt,
            source = entity.source,
            title = entity.title,
            url = entity.url,
            urlToImage = entity.urlToImage,
            isBookMarked = entity.isBookMarked
        )
    }

    fun toEntityList(articles: List<Article>): List<ArticleEntity> {
        return articles.map { toEntity(it) }
    }

    fun toDomainList(entities: List<ArticleEntity>): List<Article> {
        return entities.map { toDomain(it) }
    }
}
