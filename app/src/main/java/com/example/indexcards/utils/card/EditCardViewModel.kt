package com.example.indexcards.utils.card

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.indexcards.data.AppRepository

class EditCardViewModel(
    private val appRepository: AppRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val boxId: Long = checkNotNull(savedStateHandle["boxId"])

    var cardUiState by mutableStateOf(CardState())

    suspend fun saveCard() {
        updateUiState(cardUiState.cardDetails.copy(boxId = boxId))
        if (validateInput(cardUiState.cardDetails)) {
            appRepository.insertCard(cardUiState.cardDetails.toCard())
        }
    }

    suspend fun deleteCard(cardId: Long) {
        appRepository.deleteCard(cardId = cardId)
    }

    fun resetUiStatus() {
        updateUiState(CardDetails())
    }

    fun updateUiState(cardDetails: CardDetails) {
        cardUiState =
            CardState(
                cardDetails = cardDetails,
                isValid = validateInput(cardDetails)
            )
    }

    private fun validateInput(
        uiState: CardDetails = cardUiState.cardDetails
    ): Boolean {
        return with(uiState) {
            word.isNotBlank() && meaning.isNotBlank()
        }
    }
}