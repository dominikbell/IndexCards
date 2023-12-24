package com.example.indexcards.utils.box

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.indexcards.data.AppRepository
import com.example.indexcards.data.Box
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

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

//    fun updateUiState(box: Box) {
//        boxUiState =
//            BoxState(
//                boxDetails = BoxDetails(
//                    id = box.boxId,
//                    name = box.name,
//                    topic = box.topic,
//                    description = box.description
//                )
//            )
//    }

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