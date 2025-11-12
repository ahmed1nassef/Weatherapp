package com.nassef.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nassef.data.utilities.DbTypeConverters
//import com.nassef.data.entities.Article
import com.nassef.domain.entities.Article

@Database(entities = [Article::class] , version = 2 , exportSchema = false)
@TypeConverters(DbTypeConverters::class)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun articleDao() : ArticleDao
}