package com.example.indexcards.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.indexcards.data.AppRepository
import com.example.indexcards.utils.state.BoxDetails
import com.example.indexcards.utils.state.BoxState
import com.example.indexcards.utils.state.toBox
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** ParentClass for creating/editing a Box
 * - displays a BoxUiState
 * - upon saving, writes this BoxUiState to the database
 */
open class AppViewModel(
    val appRepository: AppRepository,
    val userPreferences: UserPreferences
) : ViewModel() {
    /** Companion object used for converting Flows to StateFlows
     */
    companion object {
        const val TIMEOUT_MILLIS = 5_000L
    }


    /** boxUiState
     * sets the Ui for viewing and editing a box
     */
    var boxUiState by mutableStateOf(BoxState())

    fun resetBoxUiState() {
        boxUiState = BoxState()
    }

    fun updateBoxUiState(boxDetails: BoxDetails) {
        boxUiState = BoxState(
            boxDetails = boxDetails,
            isValid = validateInput(boxDetails),
            validName = boxDetails.name.isNotBlank(),
            validTopic = boxDetails.topic.isNotBlank(),
        )
    }

    private fun validateInput(boxDetails: BoxDetails): Boolean {
        return (boxDetails.name.isNotBlank() && boxDetails.topic.isNotBlank())
    }


    /** functions for saving a box from the boxUiState and deleting a box using the boxId
     */
    fun saveBox() {
        viewModelScope.launch {
            if (boxUiState.isValid) {
                if (boxUiState.boxDetails.id == (-1).toLong()) {
                    updateBoxUiState(
                        boxUiState.boxDetails.copy(
                            id = appRepository.getBiggestBoxId() + 1
                        )
                    )
                }
                appRepository.upsertBox(boxUiState.boxDetails.toBox())
            }
            resetBoxUiState()
        }
    }

    fun deleteBox(boxId: Long) {
        viewModelScope.launch {
            appRepository.deleteBox(boxId = boxId)
        }
    }


    /** globalReminders, reminderIntervals, reminderTime
     * are used (both on the homeScreen and on the BoxScreen) to schedule reminders
     */
    val globalReminders: StateFlow<Boolean> = userPreferences.currentGlobalReminders
        .filterNotNull()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = DefaultPreferences.GLOBAL_REMINDERS
        )

    fun changeGlobalReminders() {
        viewModelScope.launch {
            val currentGlobalReminders = globalReminders.value
            userPreferences.saveGlobalReminders(
                globalReminders = !currentGlobalReminders
            )
        }
    }

    val reminderIntervals: StateFlow<List<Pair<Int, String>>> =
        userPreferences.currentReminderIntervals
            .filterNotNull()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = DefaultPreferences.REMINDER_INTERVALS
            )

    val reminderTime: StateFlow<Pair<Int, Int>> =
        userPreferences.currentReminderTime
            .filterNotNull()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = DefaultPreferences.REMINDER_TIME

            )
}