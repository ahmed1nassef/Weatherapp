package com.nassef.core.domain.repository.local

interface IStorageKeyValueFile {
    val storageKV: IStorageKeyValue
    suspend fun clearStorageFile()
    suspend fun deleteStorageFile(): Boolean
}