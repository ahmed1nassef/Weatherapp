package com.nassef.data.features.splashHistory.repository.local

import com.nassef.core.domain.repository.local.IStorageKeyValue
import com.nassef.data.local.StorageKeyEnum

class StartDestinationLocalDS (private val storage : IStorageKeyValue) : IStartDestinationLocalDS {
    override suspend fun saveSplashHistory(isOpened: Boolean) {
        storage.saveEntry(StorageKeyEnum.SPLASH_OPENED , isOpened)
    }

    override suspend fun isSplashOpened(): Boolean {
        return storage.readEntry(StorageKeyEnum.SPLASH_OPENED , false)
    }

}