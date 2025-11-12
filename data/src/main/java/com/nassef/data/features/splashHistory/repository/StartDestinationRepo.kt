package com.nassef.data.features.splashHistory.repository

import com.nassef.data.features.splashHistory.repository.local.IStartDestinationLocalDS
import com.nassef.domain.features.splashHistory.repository.IStartDestinationRepo

class StartDestinationRepo (private val dataset : IStartDestinationLocalDS) : IStartDestinationRepo {
    override suspend fun saveSplashHistory(isOpened: Boolean) {
        dataset.saveSplashHistory(isOpened)
    }

    override suspend fun isSplashOpenedBefore(): Boolean {
        return dataset.isSplashOpened()
    }
}