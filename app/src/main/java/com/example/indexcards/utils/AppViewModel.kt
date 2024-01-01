package com.example.indexcards.utils

import androidx.lifecycle.ViewModel
import com.example.indexcards.data.AppRepository
import com.example.indexcards.data.Box

open class AppViewModel(
    private val appRepository: AppRepository,
) : ViewModel() {
    companion object {
        const val TIMEOUT_MILLIS = 5_000L
    }
}