package com.example.indexcards.data

import android.content.Context

interface AppContainer {
    val appRepository: AppRepository
}

class AppDataContainer(
    private val context: Context
) : AppContainer {
    override val appRepository: AppRepository by lazy {
        OfflineAppRepository(AppDatabase.getDatabase(context).appDao())
    }
}