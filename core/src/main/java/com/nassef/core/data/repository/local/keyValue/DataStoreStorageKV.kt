package com.nassef.core.data.repository.local.keyValue

import androidx.datastore.core.DataStore
import com.nassef.core.R
import com.nassef.core.data.model.exception.AppException
import com.nassef.core.domain.repository.local.IStorageKeyEnum
import com.nassef.core.domain.repository.local.IStorageKeyValue
import com.nassef.core.extentions.base64Decode
import com.nassef.core.extentions.base64Encode
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey


/*internal*/ class DataStoreStorageKV(private val dataStore: DataStore<Preferences>) :
    IStorageKeyValue {

    override suspend fun <T> saveEntry(key: IStorageKeyEnum, data: T) {
        dataStore.edit {
            when (data) {
                is String -> it[stringPreferencesKey(key.keyValue)] = data
                is Int -> it[intPreferencesKey(key.keyValue)] = data
                is Boolean -> it[booleanPreferencesKey(key.keyValue)] = data
                is Float -> it[floatPreferencesKey(key.keyValue)] = data
                is Long -> it[longPreferencesKey(key.keyValue)] = data
                is ByteArray -> it[stringPreferencesKey(key.keyValue)] = data.base64Encode()
                else -> throw AppException.Local.IOOperation(R.string.error_unexpected_message)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T> readEntry(key: IStorageKeyEnum, defaultValue: T): T {
        return when (defaultValue) {
            is String -> (dataStore.data.map { it[stringPreferencesKey(key.keyValue)] }
                .firstOrNull() ?: defaultValue) as T

            is Int -> (dataStore.data.map { it[intPreferencesKey(key.keyValue)] }
                .firstOrNull() ?: defaultValue) as T

            is Boolean -> (dataStore.data.map { it[booleanPreferencesKey(key.keyValue)] }
                .firstOrNull() ?: defaultValue) as T

            is Float -> (dataStore.data.map { it[floatPreferencesKey(key.keyValue)] }
                .firstOrNull() ?: defaultValue) as T

            is Long -> (dataStore.data.map { it[longPreferencesKey(key.keyValue)] }
                .firstOrNull() ?: defaultValue) as T

            is ByteArray -> (dataStore.data.map { it[stringPreferencesKey(key.keyValue)] }
                .firstOrNull()?.base64Decode() ?: defaultValue) as T

            else -> throw AppException.Local.IOOperation(R.string.error_io_unexpected_message)
        }
    }

    override suspend fun <T> clearEntry(key: IStorageKeyEnum, defaultValue: T) {
        dataStore.edit {
            when (defaultValue) {
                is String -> it[stringPreferencesKey(key.keyValue)] = defaultValue
                is Int -> it[intPreferencesKey(key.keyValue)] = defaultValue
                is Boolean -> it[booleanPreferencesKey(key.keyValue)] = defaultValue
                is Float -> it[floatPreferencesKey(key.keyValue)] = defaultValue
                is Long -> it[longPreferencesKey(key.keyValue)] = defaultValue
                is ByteArray -> it[stringPreferencesKey(key.keyValue)] = defaultValue.base64Encode()
                else -> throw AppException.Local.IOOperation(R.string.error_io_unexpected_message)
            }
        }
    }

    override suspend fun hasEntry(key: IStorageKeyEnum): Boolean {
        var hasKey = false
        dataStore.edit { hasKey = it.contains(stringPreferencesKey(key.keyValue)) }
        return hasKey
    }
}