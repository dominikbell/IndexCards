package com.example.indexcards

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.indexcards.utils.USER_PREFERENCES_NAME
import com.example.indexcards.utils.UserPreferences

const val NUMBER_OF_LEVELS: Int = 5

val CHOICES_FOR_REMINDER_INTERVALS: List<String> = listOf("d", "w", "m")

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)

class IndexCardsApp: Application() {
    private lateinit var userPreferences: UserPreferences
    override fun onCreate() {
        super.onCreate()
        userPreferences = UserPreferences(dataStore)
    }
}