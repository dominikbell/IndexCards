package com.example.indexcards.utils.box

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.indexcards.data.AppRepository

open class BoxViewModel(
    private val appRepository: AppRepository
) : ViewModel() {

    var boxUiState by mutableStateOf(BoxState())

    fun updateUiState(boxDetails: BoxDetails) {
        boxUiState =
            BoxState(
                boxDetails = boxDetails,
                isValid = validateInput(boxDetails)
            )
    }

    companion object {
        const val TIMEOUT_MILLIS = 5_000L
    }

    open suspend fun saveItem() {
        if (validateInput()) {
            appRepository.insertBox(boxUiState.boxDetails.toBox())
        }
    }

    fun validateInput(
        uiState: BoxDetails = boxUiState.boxDetails
    ): Boolean {
        return with(uiState) {
            name.isNotBlank() && topic.isNotBlank()
        }
    }
}