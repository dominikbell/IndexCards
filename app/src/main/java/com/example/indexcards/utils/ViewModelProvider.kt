package com.example.indexcards.utils

import android.content.Context
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.indexcards.data.AppDatabase
import com.example.indexcards.data.OfflineAppRepository
import com.example.indexcards.utils.box.BoxScreenViewModel
import com.example.indexcards.utils.box.HomeScreenViewModel
import com.example.indexcards.utils.box.BoxViewModel
import com.example.indexcards.utils.card.CardViewModel
import com.example.indexcards.utils.card.EditCardViewModel
import com.example.indexcards.utils.card.NewCardViewModel
import com.example.indexcards.utils.tag.EditTagViewModel

class ViewModelProvider(
    context: Context
) {
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
