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
import com.example.indexcards.utils.box.EditBoxViewModel
import com.example.indexcards.utils.card.EditCardViewModel
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
        initializer {
            HomeScreenViewModel(
                OfflineAppRepository(AppDatabase.getDatabase(context).appDao()),
            )
        }
        initializer {
            EditBoxViewModel(
                OfflineAppRepository(AppDatabase.getDatabase(context).appDao()),
                this.createSavedStateHandle(),
            )
        }
        initializer {
            BoxScreenViewModel(
                OfflineAppRepository(AppDatabase.getDatabase(context).appDao()),
                this.createSavedStateHandle(),
            )
        }
        initializer {
            BoxViewModel(
                OfflineAppRepository(AppDatabase.getDatabase(context).appDao()),
            )
        }
        initializer {
            EditCardViewModel(
                OfflineAppRepository(AppDatabase.getDatabase(context).appDao()),
                this.createSavedStateHandle(),
            )
        }
        initializer {
            EditTagViewModel(
                OfflineAppRepository(AppDatabase.getDatabase(context).appDao()),
                this.createSavedStateHandle(),
            )
        }
    }
}
