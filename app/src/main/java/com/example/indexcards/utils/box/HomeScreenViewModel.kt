package com.example.indexcards.utils.box

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    var addBoxUiState by mutableStateOf(BoxState())

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
            appRepository.deleteBox(boxToBeDeleted!!)
            boxToBeDeleted = null
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class HomeUiState(
    val boxList: List<Box> = listOf()
)