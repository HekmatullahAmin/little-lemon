package com.hekmatullahamin.littlelemon.data.repositories

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import com.hekmatullahamin.littlelemon.ui.screens.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {

    private companion object {
        private val THEME_MODE_KEY = intPreferencesKey("theme_mode")
        const val TAG = "UserPreferencesRepo"
    }

    val themeMode: Flow<ThemeMode> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val themeOrdinal = preferences[THEME_MODE_KEY] ?: ThemeMode.LIGHT.ordinal
            ThemeMode.values()[themeOrdinal]
        }

    suspend fun saveThemeMode(themeMode: ThemeMode) {
        dataStore.edit { preferences ->
            preferences[THEME_MODE_KEY] = themeMode.ordinal
        }
    }
}