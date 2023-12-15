package com.example.indexcards

import android.app.Application
import com.example.indexcards.data.AppContainer
import com.example.indexcards.data.AppDataContainer

class IndexCardsApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}