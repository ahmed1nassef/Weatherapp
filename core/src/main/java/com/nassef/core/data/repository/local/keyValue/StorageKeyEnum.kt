package com.nassef.core.data.repository.local.keyValue

import com.nassef.core.domain.repository.local.IStorageKeyEnum

internal enum class StorageKeyEnum(override val keyValue: String) : IStorageKeyEnum {
    ACTIVATE_TOKEN("activate_token"),
    UNDEFINED("");

    companion object {
        fun find(signature: String) = entries.find { it.keyValue == signature } ?: UNDEFINED
    }
}