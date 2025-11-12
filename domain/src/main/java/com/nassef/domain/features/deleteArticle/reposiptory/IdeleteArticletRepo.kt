package com.nassef.domain.features.deleteArticle.reposiptory

import com.nassef.domain.entities.Article

interface IdeleteArticletRepo {
    suspend fun deleteArtcileById(id : Int)
    suspend fun deleteArtcile(article: Article)

}