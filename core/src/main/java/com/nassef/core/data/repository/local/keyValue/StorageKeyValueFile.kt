package com.nassef.core.data.repository.local.keyValue

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.nassef.core.domain.repository.local.IStorageKeyValue
import com.nassef.core.domain.repository.local.IStorageKeyValueFile
import java.io.File

class StorageKeyValueFile( private val context: Context) : IStorageKeyValueFile {
    companion object {
        private const val DATA_STORE_FILE_NAME: String = "pets_core_storageDS"
    }

    private val Context.dataStore by preferencesDataStore(name = DATA_STORE_FILE_NAME)
    override val storageKV: IStorageKeyValue
        get() = DataStoreStorageKV(context.dataStore)

    override suspend fun clearStorageFile() { // empty
        context.dataStore.edit { it.clear() }
    }

    override suspend fun deleteStorageFile(): Boolean {
        val file = File(context.filesDir, "/datastore/$DATA_STORE_FILE_NAME.preferences_pb")
        return file.delete()
    }
}