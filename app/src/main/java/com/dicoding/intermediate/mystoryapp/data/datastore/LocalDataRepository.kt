package com.dicoding.intermediate.mystoryapp.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalDataRepository private constructor(private val dataStore: DataStore<Preferences>) {
    private val localeKey = stringPreferencesKey("locale")

    fun getLocaleSetting(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[localeKey] ?: "en"
        }
    }

    suspend fun saveLocaleSetting(localeName: String) {
        dataStore.edit { preferences ->
            preferences[localeKey] = localeName
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: LocalDataRepository? = null

        fun getInstance(dataStore: DataStore<Preferences>): LocalDataRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: createInstance(dataStore).also { INSTANCE = it }
            }
        }

        private fun createInstance(dataStore: DataStore<Preferences>): LocalDataRepository {
            return LocalDataRepository(dataStore)
        }
    }
}
