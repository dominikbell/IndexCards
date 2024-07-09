package com.example.indexcards.utils

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.indexcards.data.AppDatabase
import com.example.indexcards.data.OfflineAppRepository
import com.example.indexcards.utils.box.BoxScreenViewModel
import com.example.indexcards.utils.home.HomeScreenViewModel

class ViewModelProvider(
    context: Context,
) {
    private val Context.dataStore by preferencesDataStore(USER_PREFERENCES_NAME)

    val factory = viewModelFactory {
        /** ParentClass for the other two ViewModels **/
        initializer {
            AppViewModel(
                OfflineAppRepository(AppDatabase.getDatabase(context).appDao()),
            )
        }

        /** For HomeScreen **/
        initializer {
            HomeScreenViewModel(
                OfflineAppRepository(AppDatabase.getDatabase(context).appDao()),
                UserPreferences(context.dataStore)
            )
        }

        /** For BoxScreen **/
        initializer {
            BoxScreenViewModel(
                OfflineAppRepository(AppDatabase.getDatabase(context).appDao()),
                this.createSavedStateHandle(),
                UserPreferences(context.dataStore)
            )
        }
    }
}
