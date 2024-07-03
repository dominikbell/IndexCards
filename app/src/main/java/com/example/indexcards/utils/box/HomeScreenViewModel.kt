package com.example.indexcards.utils.box

import androidx.lifecycle.viewModelScope
import com.example.indexcards.data.AppRepository
import com.example.indexcards.data.Box
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    appRepository: AppRepository,
) : BoxViewModel(
    appRepository = appRepository,
) {
    private val selectedBoxId = MutableStateFlow((-1).toLong())
    val selectedBox = MutableStateFlow(emptyBox)

    val uiBoxList: StateFlow<UiBoxList> =
        appRepository.getAllBoxesStream()
            .filterNotNull()
            .map {
                UiBoxList(it)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = UiBoxList()
            )

    fun setCurrentBox(boxId: Long) {
        viewModelScope.launch {
            selectedBoxId.update {
                boxId
            }

            selectedBox.update {
                appRepository.getBox(selectedBoxId.value)
                    .filterNotNull()
                    .first()
            }
        }
    }

    fun resetCurrentBox() {
        viewModelScope.launch {
            selectedBoxId.update { -1 }

            selectedBox.update { emptyBox }
        }
    }

    fun deleteBox() {
        viewModelScope.launch {
            appRepository.deleteBox(selectedBoxId.value)
        }
    }
}

data class UiBoxList(
    val boxList: List<Box> = listOf()
)