package com.example.indexcards.utils.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.indexcards.data.AppRepository
import com.example.indexcards.data.Box
import com.example.indexcards.utils.AppViewModel
import com.example.indexcards.utils.DefaultPreferences
import com.example.indexcards.utils.UserPreferences
import com.example.indexcards.utils.box.emptyBox
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
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
    userPreferences = userPreferences
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


    /** Get the number of cards in a box of a level to only create a notification
     * if the level has any cards in it */
    suspend fun getNumberOfCardsOfLevelInBox(boxId: Long, level: Int): Int {
        return appRepository.getNumberOfCardsOfLevelInBox(boxId, level)
    }


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
    var uiSettings by mutableStateOf(UiSettings())

    fun updateCurrentLevel(newLevel: Int) {
        currentLevel = newLevel
    }

    fun resetCurrentLevel() {
        currentLevel = -1
    }


    /** CurrentLevel
     * used for setting the reminder interval for a specific level
     */
    var currentLevel by mutableIntStateOf(-1)

    fun updateUiSettings(settingsDetails: SettingsDetails) {
        uiSettings = UiSettings(
            settingsDetails,
            isValid = settingsDetails.isValid()
        )
    }

    fun resetUiSettings() {
        uiSettings = UiSettings()
    }

    /** Flow of the username, just a gimmick feature */
    val userName: StateFlow<String> = userPreferences.currentUserName
        .filterNotNull()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = DefaultPreferences.USER_NAME
        )


    /** save the Preferences from the current UiSettings
     */
    fun savePreferences(doReset: Boolean = false) {
        if (uiSettings.isValid) {
            viewModelScope.launch {
                userPreferences.saveNewPreferences(
                    userName = uiSettings.settingsDetails.userName,
                    globalReminders = uiSettings.settingsDetails.globalReminders,
                    reminderIntervals = uiSettings.settingsDetails.reminderIntervals
                )

                if (doReset) {
                    resetUiSettings()
                    resetCurrentLevel()
                }
            }
        }
    }
}
