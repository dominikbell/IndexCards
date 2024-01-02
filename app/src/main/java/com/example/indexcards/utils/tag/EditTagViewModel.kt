package com.example.indexcards.utils.tag

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import com.example.indexcards.data.AppRepository
import com.example.indexcards.utils.box.BoxDetailViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first

class EditTagViewModel(
    private val appRepository: AppRepository,
    savedStateHandle: SavedStateHandle
) : BoxDetailViewModel(
    appRepository = appRepository,
    savedStateHandle = savedStateHandle
) {
    var tagUiState by mutableStateOf(TagState())
    var colorUiState by mutableStateOf(UiColorState())
    var currentTag by mutableStateOf(emptyTag)

    suspend fun saveTag() {
        /* TODO: Ensure that tag does not already exist */
        updateUiState(tagUiState.tagDetails.copy(boxId = boxId, color = colorUiState.color))
        if (validateInput(tagUiState.tagDetails)) {
            appRepository.insertTag(tagUiState.tagDetails.toTag())
        }
    }

    suspend fun updateTag() {
        updateUiState(tagUiState.tagDetails.copy(color = colorUiState.color))
        if (validateInput(tagUiState.tagDetails)) {
            appRepository.updateTag(tagUiState.tagDetails.toTag())
        }
    }

    suspend fun setCurrentTag(tagId: Long) {
        currentTag = appRepository.getTag(tagId = tagId)
            .filterNotNull()
            .first()
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

    fun setColor(color: String) {
        colorUiState =
            if (validateColor(color)) {
                UiColorState(color = color)
            } else {
                UiColorState(
//            "#000000"
                )
            }
    }

    suspend fun deleteTag() {
        appRepository.deleteTag(tagUiState.tagDetails.id)
    }

    private fun validateColor(color: String): Boolean {
        return (color.first() == '#' && (color.length == 7 || color.length == 9))
    }

    private fun validateInput(
        newTagUiState: TagDetails = tagUiState.tagDetails
    ): Boolean {
        return with(newTagUiState) {
            text.isNotBlank() && color.isNotBlank()
        }
    }
}

data class UiColorState(
    val color: String = "#FFFFFF"
)

fun UiColorState.toColor(): Color {
    return Color(android.graphics.Color.parseColor(color))
}
