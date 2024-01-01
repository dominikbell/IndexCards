package com.example.indexcards.utils.box

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.indexcards.data.AppRepository
import com.example.indexcards.data.Box
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeScreenViewModel(
    appRepository: AppRepository,
) : BoxViewModel(
    appRepository = appRepository,
) {
    var idOfBoxToBeDeleted by mutableLongStateOf(-1)
    var boxToBeDeleted by mutableStateOf(emptyBox)

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

    suspend fun getBoxToBeDeleted(boxId: Long) {
        boxToBeDeleted = appRepository.getBox(boxId)
            .filterNotNull()
            .stateIn(scope = viewModelScope)
            .value
    }

    fun resetIdOfBoxToBeDeleted() {
        idOfBoxToBeDeleted = -1
    }
}

data class UiBoxList(
    val boxList: List<Box> = listOf()
)