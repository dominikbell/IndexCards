package com.example.indexcards.utils

import android.content.Context
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.indexcards.data.AppDatabase
import com.example.indexcards.data.OfflineAppRepository
import com.example.indexcards.ui.home.HomeScreenViewModel

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
    }
}
