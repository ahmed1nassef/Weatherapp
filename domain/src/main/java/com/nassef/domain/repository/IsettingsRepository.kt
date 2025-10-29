package com.nassef.domain.repository

interface IsettingsRepository {
    suspend fun saveLangCode(languageCode : String)
    suspend fun getLang(): String
    suspend fun saveChoosedTheme(languageCode : String)
    suspend fun getChoosedTheme(): String
    suspend fun useDynamicColor() : Boolean
    suspend fun getDarkModePref(): String
    suspend fun saveDarkModePref(): String
}