package com.nassef.data.local

import com.nassef.core.domain.repository.local.IStorageKeyEnum

internal enum class StorageKeyEnum(override val keyValue: String) : IStorageKeyEnum {
    SPLASH_OPENED("splash_history_state"),
    UNDEFINED("");

    companion object {
        fun find(signature: String) = entries.find { it.keyValue == signature } ?: UNDEFINED
    }
}