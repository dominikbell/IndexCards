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

private val Context.dataStore by preferencesDataStore(USER_PREFERENCES_NAME)

/** ViewModelProvider
 * created the viewmodels and provides them with the needed repository and preferences
 */
class ViewModelProvider(
    context: Context,
) {

    val factory = viewModelFactory {
        /** ParentClass for the other two ViewModels **/
        initializer {
            AppViewModel(
                OfflineAppRepository(AppDatabase.getDatabase(context).appDao()),
                userPreferences = UserPreferences(context.dataStore),
            )
        }

        /** For HomeScreen **/
        initializer {
            HomeScreenViewModel(
                OfflineAppRepository(AppDatabase.getDatabase(context).appDao()),
                userPreferences = UserPreferences(context.dataStore),
            )
        }

        /** For BoxScreen **/
        initializer {
            BoxScreenViewModel(
                OfflineAppRepository(AppDatabase.getDatabase(context).appDao()),
                this.createSavedStateHandle(),
                userPreferences = UserPreferences(context.dataStore),
            )
        }
    }
}
