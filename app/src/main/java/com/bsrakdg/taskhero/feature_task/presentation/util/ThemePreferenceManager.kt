package com.bsrakdg.taskhero.feature_task.presentation.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_prefs")

object ThemePreferenceManager {

    private val THEME_KEY = booleanPreferencesKey("dark_mode_enabled")

    suspend fun setDarkMode(context: Context, isDarkMode: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[THEME_KEY] = isDarkMode
        }
    }

    fun getDarkModeFlow(context: Context): Flow<Boolean> {
        return context.dataStore.data
            .map { prefs -> prefs[THEME_KEY] ?: false }
    }
}




