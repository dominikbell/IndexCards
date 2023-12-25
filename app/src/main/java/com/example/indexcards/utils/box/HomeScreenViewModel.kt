package com.example.indexcards.utils.box

import androidx.lifecycle.viewModelScope
import com.example.indexcards.data.AppRepository
import com.example.indexcards.data.Box
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeScreenViewModel(
    private val appRepository: AppRepository
) : BoxViewModel(
    appRepository = appRepository
) {
    var boxToBeDeleted: Box? = null

    val homeUiState: StateFlow<HomeUiState> =
        appRepository.getAllBoxesStream().map {
            HomeUiState(it)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = HomeUiState()
        )

    suspend fun deleteBox() {
        if (boxToBeDeleted != null) {
            appRepository.deleteBox(boxToBeDeleted!!.boxId)
            boxToBeDeleted = null
        }
    }

    fun resetUiStatus() {
        updateUiState(BoxDetails())
    }
}

data class HomeUiState(
    val boxList: List<Box> = listOf()
)