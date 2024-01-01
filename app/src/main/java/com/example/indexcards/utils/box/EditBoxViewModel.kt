package com.example.indexcards.utils.box

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.indexcards.data.AppRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditBoxViewModel(
    appRepository: AppRepository,
    savedStateHandle: SavedStateHandle
) : BoxViewModel(
    appRepository = appRepository,
) {
    val boxId: Long = checkNotNull(savedStateHandle["boxId"])

    var currentBox by mutableStateOf(emptyBox)

    init {
        viewModelScope.launch {
            currentBox = appRepository.getBox(boxId)
                .filterNotNull()
                .first()

            boxUiState = currentBox.toBoxState()
        }
    }
}