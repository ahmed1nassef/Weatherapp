package com.nassef.domain.features.splashHistory.repository

interface IStartDestinationRepo {
    suspend fun saveSplashHistory(isOpened : Boolean)
    suspend fun isSplashOpenedBefore(): Boolean
}