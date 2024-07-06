package com.example.indexcards.utils.box

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.indexcards.data.AppRepository
import com.example.indexcards.data.Box
import com.example.indexcards.utils.DefaultPreferences
import com.example.indexcards.utils.UiPreferences
import com.example.indexcards.utils.UserPreferences
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
    private val userPreferences: UserPreferences
) : BoxViewModel(
    appRepository = appRepository,
) {
    private val selectedBoxId = MutableStateFlow((-1).toLong())
    var homeScreenState: HomeScreenState by mutableStateOf(HomeScreenState.MAIN)
    val selectedBox = MutableStateFlow(emptyBox)
    var uiPreferences by mutableStateOf(UiPreferences())

    private val userName: StateFlow<String> = userPreferences.currentUserName
        .filterNotNull()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = DefaultPreferences.USER_NAME
        )

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

    fun updateHomeScreenState(newState: HomeScreenState) {
        homeScreenState = newState
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

    fun updateUiUserName(newName: String) {
        uiPreferences = uiPreferences.copy(userName = newName)
    }

    fun setCurrentUiPreferences() {
        viewModelScope.launch {
            uiPreferences = uiPreferences.copy(
                userName = userPreferences.currentUserName.first(),
                globalReminders = userPreferences.currentGlobalReminders.first()
            )
        }
    }

    fun savePreferences() {
        viewModelScope.launch {
            userPreferences.saveNewPreferences(
                userName = uiPreferences.userName,
                globalReminders = uiPreferences.globalReminders,
                reminderIntervals = uiPreferences.reminderIntervals
            )
        }
    }
}

data class UiBoxList(
    val boxList: List<Box> = listOf()
)

sealed interface HomeScreenState {
    data object MAIN : HomeScreenState
    data object SETTINGS : HomeScreenState
    data object STATISTICS : HomeScreenState
}
