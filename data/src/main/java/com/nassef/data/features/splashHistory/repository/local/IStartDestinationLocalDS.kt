package com.nassef.data.features.splashHistory.repository.local

interface IStartDestinationLocalDS {
    suspend fun saveSplashHistory(isOpened : Boolean)
    suspend fun isSplashOpened() : Boolean
}