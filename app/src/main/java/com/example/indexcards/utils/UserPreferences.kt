package com.example.indexcards.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.example.indexcards.NUMBER_OF_LEVELS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

const val USER_PREFERENCES_NAME = "user_preferences"

object DefaultPreferences {
    const val USER_NAME: String = "Unknown"
    const val GLOBAL_REMINDERS: Boolean = true
    val REMINDER_INTERVALS: List<String> = listOf("1d", "3d", "1w", "2w", "1m")
}

data class UiPreferences(
    val userName: String = DefaultPreferences.USER_NAME,
    val globalReminders: Boolean = DefaultPreferences.GLOBAL_REMINDERS,
    val reminderIntervals: List<String> = DefaultPreferences.REMINDER_INTERVALS
)

class UserPreferences(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val USER_NAME = stringPreferencesKey("user_name")
        val GLOBAL_REMINDERS = booleanPreferencesKey("global_reminders")
        val REMINDER_INTERVALS = (0 .. NUMBER_OF_LEVELS).map {k ->
            stringPreferencesKey(
                "reminder_intervals$k"
            )
        }.toList()
    }

    suspend fun saveNewPreferences(
        userName: String,
        globalReminders: Boolean,
        reminderIntervals: List<String>
    ) {
        dataStore.edit { preferences ->
            preferences[USER_NAME] = userName
            preferences[GLOBAL_REMINDERS] = globalReminders
            (0 .. NUMBER_OF_LEVELS).map { k ->
                preferences[REMINDER_INTERVALS[k]] = reminderIntervals[k]
            }
        }
    }

    val currentUserName: Flow<String> =
        dataStore.data.map { preferences ->
            preferences[USER_NAME] ?: DefaultPreferences.USER_NAME
        }

    val username = dataStore.data

    val currentGlobalReminders: Flow<Boolean> =
        dataStore.data.map { preferences ->
            preferences[GLOBAL_REMINDERS] ?: DefaultPreferences.GLOBAL_REMINDERS
        }
}