package com.example.indexcards.utils.box

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.indexcards.data.AppRepository
import com.example.indexcards.utils.AppViewModel
import kotlinx.coroutines.launch

open class BoxViewModel(
    val appRepository: AppRepository,
) : AppViewModel(
    appRepository = appRepository,
) {
    var boxUiState by mutableStateOf(BoxState())

    suspend fun saveBox() {
        if (boxUiState.isValid) {
            appRepository.upsertBox(boxUiState.boxDetails.toBox())
        }
    }

    suspend fun deleteBox(boxId: Long) {
        appRepository.deleteBox(boxId)
    }

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
}