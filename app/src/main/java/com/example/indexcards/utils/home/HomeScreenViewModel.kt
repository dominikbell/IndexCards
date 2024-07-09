package com.example.indexcards.utils.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.indexcards.data.AppRepository
import com.example.indexcards.data.Box
import com.example.indexcards.utils.AppViewModel
import com.example.indexcards.utils.DefaultPreferences
import com.example.indexcards.utils.UiPreferences
import com.example.indexcards.utils.UserPreferences
import com.example.indexcards.utils.box.emptyBox
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


/**
 * ViewModel for the HomeScreen
 * - handles Preferences & Statistics (soon)
 * - displays list of all boxes
 * - adds and deletes boxes
 */
class HomeScreenViewModel(
    appRepository: AppRepository,
    private val userPreferences: UserPreferences
) : AppViewModel(
    appRepository = appRepository,
) {
    /** homeScreenState
     * used for navigating between main, preferences, statistics
     */
    var homeScreenState: HomeScreenState by mutableStateOf(HomeScreenState.MAIN)

    fun updateHomeScreenState(newState: HomeScreenState) {
        homeScreenState = newState
    }


    /** uiBoxList
     * StateFlow from database which displays the boxes
     */
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


    /** currentBox
     * used to select a box to go to BoxScreen (via boxId) or to delete a box
     */
    val currentBox = MutableStateFlow(emptyBox)

    fun setCurrentBox(box: Box) {
        viewModelScope.launch {
            currentBox.update { box }
        }
    }

    fun resetCurrentBox() {
        viewModelScope.launch {
            currentBox.update { emptyBox }
        }
    }


    /** uiPreferences
     * Used for displaying and changing the preferences
     */
    var uiPreferences by mutableStateOf(UiPreferences())

    private val userName: StateFlow<String> = userPreferences.currentUserName
        .filterNotNull()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = DefaultPreferences.USER_NAME
        )

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
