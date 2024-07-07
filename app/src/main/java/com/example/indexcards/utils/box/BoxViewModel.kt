package com.example.indexcards.utils.box

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.indexcards.data.AppRepository
import com.example.indexcards.utils.AppViewModel
import kotlinx.coroutines.launch


/** ParentClass for creating/editing a Box
 * - displays a UiState
 * - upon saving, writes this UiState to the database
 */
open class BoxViewModel(
    appRepository: AppRepository,
) : AppViewModel(
    appRepository = appRepository,
) {
    /** boxUiState
     * sets the Ui for viewing and editing a box
     */
    var boxUiState by mutableStateOf(BoxState())

    fun resetUiState() {
        boxUiState = BoxState()
    }

    fun updateUiState(boxDetails: BoxDetails) {
        viewModelScope.launch {
            boxUiState = BoxState(
                boxDetails = boxDetails,
                isValid = validateInput(boxDetails)
            )
        }
    }

    private fun validateInput(boxDetails: BoxDetails): Boolean {
        return (boxDetails.name.isNotBlank() && boxDetails.topic.isNotBlank() && boxDetails.id != (-1).toLong())
    }


    /** functions for saving a box from the boxUiState and deleting a box using the boxId
     */
    fun saveBox() {
        viewModelScope.launch {
            if (boxUiState.isValid) {
                appRepository.upsertBox(boxUiState.boxDetails.toBox())
            } else {
                /* TODO: implement what to do when entry is not valid */
            }
        }
    }

    fun deleteBox(boxId: Long) {
        viewModelScope.launch {
            appRepository.deleteBox(boxId = boxId)
        }
    }
}