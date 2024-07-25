package com.example.indexcards.utils.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.indexcards.NUMBER_OF_LEVELS
import com.example.indexcards.data.AppRepository
import com.example.indexcards.data.Box
import com.example.indexcards.data.BoxWithCards
import com.example.indexcards.utils.AppViewModel
import com.example.indexcards.utils.DefaultPreferences
import com.example.indexcards.utils.UserPreferences
import com.example.indexcards.utils.box.UiBoxWithCards
import com.example.indexcards.utils.box.emptyBox
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
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
@OptIn(ExperimentalCoroutinesApi::class)
class HomeScreenViewModel(
    appRepository: AppRepository,
    userPreferences: UserPreferences
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


    /** Have this here to delete all the memos */
    val boxWithCards: StateFlow<UiBoxWithCards> =
        currentBox.flatMapLatest { box ->
            when (box) {
                emptyBox -> flow {
                    emit(
                        UiBoxWithCards(
                            box = emptyBox,
                            cardList = listOf()
                        )
                    )
                }

                else -> {
                    appRepository.getBoxWithCardsStream(box.boxId)
                        .filterNotNull()
                        .map { boxStream ->
                            UiBoxWithCards(
                                box = boxStream.box,
                                cardList = boxStream.cards
                            )
                        }
                }
            }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = UiBoxWithCards()
            )


    /** uiPreferences
     * (userName, reminderIntervals, reminderTime)
     * Used for displaying and changing the preferences
     * each has their own update, reset, and save function
     */

    /** uiUserName */
    var uiUserName by mutableStateOf(UiUserName())

    fun updateUiUserName(newName: String) {
        uiUserName = UiUserName(
            userName = newName,
            isValid = newName.isNotBlank()
        )
    }

    fun resetUiUserName() {
        uiUserName = UiUserName()
    }

    fun saveUserName(doReset: Boolean = false) {
        viewModelScope.launch {
            if (uiUserName.isValid) {
                userPreferences.saveNewUserName(uiUserName.userName)
            }
            if (doReset) {
                resetUiUserName()
            }
        }
    }

    /** uiReminderIntervals */
    var uiReminderIntervals by mutableStateOf(UiReminderIntervals())

    fun updateUiReminderIntervals(intervals: List<Pair<Int, String>>) {
        uiReminderIntervals = UiReminderIntervals(
            reminderIntervals = intervals,
            isValid = (intervals.size == NUMBER_OF_LEVELS && intervals.all { it.first > 0 })
        )
    }

    fun resetUiReminderIntervals() {
        uiReminderIntervals = UiReminderIntervals()
    }

    fun saveReminderIntervals(doReset: Boolean = false) {
        viewModelScope.launch {
            if (uiReminderIntervals.isValid) {
                userPreferences.saveReminderIntervals(uiReminderIntervals.reminderIntervals)
            }
            if (doReset) {
                resetUiReminderIntervals()
            }
        }
    }

    /** The UiState for ReminderTime is handled by the android widget, so we only need
     * the function to save it to the preferences file */
    fun saveReminderTime(hour: Int, minute: Int) {
        viewModelScope.launch {
            userPreferences.saveReminderTime(Pair(hour, minute))
        }
    }

    /** Flow of the username, just a gimmick feature */
    val userName: StateFlow<String> = userPreferences.currentUserName
        .filterNotNull()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = DefaultPreferences.USER_NAME
        )


    /** CurrentLevel
     * used for setting the reminder interval for a specific level
     */
    var currentLevel by mutableIntStateOf(-1)

    fun updateCurrentLevel(newLevel: Int) {
        currentLevel = newLevel
    }

    fun resetCurrentLevel() {
        currentLevel = -1
    }
}
