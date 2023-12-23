package com.example.indexcards.utils

import android.content.Context
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.indexcards.data.AppDatabase
import com.example.indexcards.data.OfflineAppRepository
import com.example.indexcards.utils.box.HomeScreenViewModel
import com.example.indexcards.utils.box.BoxViewModel
import com.example.indexcards.utils.box.EditBoxViewModel

class AppViewModelProvider(
    context: Context
) {
    val factory = viewModelFactory {
        initializer {
            /* Very hot fix */
            HomeScreenViewModel(
                OfflineAppRepository(AppDatabase.getDatabase(context).appDao())
            )
        }
        initializer {
            BoxViewModel(
                OfflineAppRepository(AppDatabase.getDatabase(context).appDao())
            )
        }
        initializer {
            EditBoxViewModel(
                OfflineAppRepository(AppDatabase.getDatabase(context).appDao()),
                this.createSavedStateHandle(),
            )
        }
    }
}
