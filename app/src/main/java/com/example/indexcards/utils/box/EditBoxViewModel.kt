package com.example.indexcards.utils.box

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.indexcards.data.AppRepository
import com.example.indexcards.data.Box
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EditBoxViewModel(
    appRepository: AppRepository,
    savedStateHandle: SavedStateHandle
) : BoxViewModel(
    appRepository = appRepository,
) {
    val boxId: Long = checkNotNull(savedStateHandle["boxId"])

    var currentBox by mutableStateOf(emptyBox)
    var idOfCardToBeDeleted by mutableLongStateOf(-1)

//    val currentBox: StateFlow<Box> =
//        appRepository.getBox(boxId)
//            .filterNotNull()
//            .map { it }
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
//                initialValue = emptyBox
//            )

    init {
        viewModelScope.launch {
            currentBox = appRepository.getBox(boxId)
                .filterNotNull()
                .first()

            boxUiState = currentBox
                .toBoxState()
        }
    }
}