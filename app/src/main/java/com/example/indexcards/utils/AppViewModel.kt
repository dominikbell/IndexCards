package com.example.indexcards.utils

import androidx.lifecycle.ViewModel
import com.example.indexcards.data.AppRepository
import com.example.indexcards.data.Box

open class AppViewModel(
    private val appRepository: AppRepository,
) : ViewModel() {
    val emptyBox: Box = Box(
        boxId = -1,
        name = "EMPTY BOX",
        topic = "EMPTY BOX",
        description = "EMPTY BOX",
        dateAdded = 0,
    )

    companion object {
        const val TIMEOUT_MILLIS = 5_000L
    }
}