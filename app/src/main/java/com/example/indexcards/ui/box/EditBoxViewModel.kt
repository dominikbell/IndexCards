package com.example.indexcards.ui.box

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.indexcards.data.AppRepository
import com.example.indexcards.data.Box
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class EditBoxViewModel(
    private val appRepository: AppRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val boxId: Long = checkNotNull(savedStateHandle["boxId"])

    val boxUiState: StateFlow<BoxUiState> =
        appRepository.getBoxStream(boxId)
            .filterNotNull()
            .map {
                BoxUiState(box = it)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = BoxUiState()
            )

    suspend fun deleteBox() {
        appRepository.deleteBox(boxUiState.value.box)
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class BoxUiState(
    val box: Box = Box(
        boxId = 0,
        name = "",
        topic = "",
        description = "",
        dateAdded = 0
    )
)