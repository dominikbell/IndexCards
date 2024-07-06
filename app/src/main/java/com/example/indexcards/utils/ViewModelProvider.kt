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
import com.example.indexcards.utils.box.BoxViewModel
import com.example.indexcards.utils.card.CardViewModel
import com.example.indexcards.utils.card.EditCardViewModel
import com.example.indexcards.utils.card.NewCardViewModel
import com.example.indexcards.utils.tag.EditTagViewModel

class ViewModelProvider(
    context: Context,
) {
    private val Context.dataStore by preferencesDataStore(USER_PREFERENCES_NAME)

    val factory = viewModelFactory {
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

        /** For BoxScreen and EditBoxScreen **/
        initializer {
            BoxViewModel(
                OfflineAppRepository(AppDatabase.getDatabase(context).appDao()),
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

        /** For CardDialog */
        initializer {
            CardViewModel(
                OfflineAppRepository(AppDatabase.getDatabase(context).appDao()),
                this.createSavedStateHandle(),
            )
        }
        initializer {
            EditCardViewModel(
                OfflineAppRepository(AppDatabase.getDatabase(context).appDao()),
                this.createSavedStateHandle(),
            )
        }
        initializer {
            NewCardViewModel(
                OfflineAppRepository(AppDatabase.getDatabase(context).appDao()),
                this.createSavedStateHandle(),
            )
        }
        /** For TagDialog */
        initializer {
            EditTagViewModel(
                OfflineAppRepository(AppDatabase.getDatabase(context).appDao()),
                this.createSavedStateHandle(),
            )
        }
    }
}
