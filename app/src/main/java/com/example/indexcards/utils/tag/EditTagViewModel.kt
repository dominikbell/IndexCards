package com.example.indexcards.utils.tag

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.indexcards.data.AppRepository

class EditTagViewModel(
    private val appRepository: AppRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val boxId: Long = checkNotNull(savedStateHandle["boxId"])
    var tagUiState by mutableStateOf(TagState())

    suspend fun saveTag() {
        /* TODO: Ensure that tag does not already exist */
        updateUiState(tagUiState.tagDetails.copy(boxId = boxId))
        if (validateInput(tagUiState.tagDetails)) {
            appRepository.insertTag(tagUiState.tagDetails.toTag())
        }
    }

    fun resetUiState() {
        updateUiState(TagDetails())
    }

    fun updateUiState(tagDetails: TagDetails) {
        tagUiState = TagState(
            tagDetails = tagDetails,
            isValid = validateInput()
        )
    }

    private fun validateInput(
        newTagUiState: TagDetails = tagUiState.tagDetails
    ): Boolean {
        return with(newTagUiState) {
            text.isNotBlank() && color.isNotBlank()
        }
    }
}
