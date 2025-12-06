package com.nassef.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nassef.data.utilities.DbTypeConverters

@Database(entities = [ArticleEntity::class], version = 3, exportSchema = false)
@TypeConverters(DbTypeConverters::class)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}