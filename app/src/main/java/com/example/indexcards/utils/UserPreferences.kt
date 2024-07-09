package com.example.indexcards.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.indexcards.NUMBER_OF_LEVELS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

const val USER_PREFERENCES_NAME = "user_preferences"

object DefaultPreferences {
    const val USER_NAME: String = "Unknown"
    const val GLOBAL_REMINDERS: Boolean = true
    val REMINDER_INTERVALS: List<Pair<Int, String>> = listOf(
        Pair(1, "d"),
        Pair(3, "d"),
        Pair(1, "w"),
        Pair(2, "w"),
        Pair(1, "m"),
    )
}

class UserPreferences(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val USER_NAME = stringPreferencesKey("user_name")
        val GLOBAL_REMINDERS = booleanPreferencesKey("global_reminders")
        val REMINDER_INTERVALS = (0..<NUMBER_OF_LEVELS).map { k ->
            Pair(
            intPreferencesKey("reminder_int_$k"),
            stringPreferencesKey("reminder_str_$k")
            )
        }.toList()
    }

    suspend fun saveNewPreferences(
        userName: String,
        globalReminders: Boolean,
        reminderIntervals: List<Pair<Int, String>>
    ) {
        dataStore.edit { preferences ->
            preferences[USER_NAME] = userName
            preferences[GLOBAL_REMINDERS] = globalReminders
            (0..<NUMBER_OF_LEVELS).map { k ->
                preferences[REMINDER_INTERVALS[k].first] = reminderIntervals[k].first
                preferences[REMINDER_INTERVALS[k].second] = reminderIntervals[k].second
            }
        }
    }

    val currentUserName: Flow<String> =
        dataStore.data.map { preferences ->
            preferences[USER_NAME] ?: DefaultPreferences.USER_NAME
        }

    val currentGlobalReminders: Flow<Boolean> =
        dataStore.data.map { preferences ->
            preferences[GLOBAL_REMINDERS] ?: DefaultPreferences.GLOBAL_REMINDERS
        }

    val currentReminderIntervals: Flow<List<Pair<Int, String>>> =
        dataStore.data.map { preferences ->
            (0..<NUMBER_OF_LEVELS).map { k ->
                Pair(
                    preferences[REMINDER_INTERVALS[k].first] ?: DefaultPreferences.REMINDER_INTERVALS[k].first,
                    preferences[REMINDER_INTERVALS[k].second] ?: DefaultPreferences.REMINDER_INTERVALS[k].second,
                )
            }
        }
}