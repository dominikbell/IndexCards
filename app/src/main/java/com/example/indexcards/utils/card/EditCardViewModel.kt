package com.example.indexcards.utils.card

import androidx.lifecycle.SavedStateHandle
import com.example.indexcards.data.AppRepository
import com.example.indexcards.data.TagCardCrossRef

open class EditCardViewModel(
    private val appRepository: AppRepository,
    savedStateHandle: SavedStateHandle
) : CardViewModel(
    appRepository = appRepository,
    savedStateHandle = savedStateHandle
) {
    open suspend fun saveCard() {
        updateUiState(cardUiState.cardDetails.copy(boxId = boxId))
        if (validateInput(cardUiState.cardDetails)) {
            appRepository.upsertCard(cardUiState.cardDetails.toCard())
        }
    }

    suspend fun deleteCard(cardId: Long) {
        appRepository.deleteCard(cardId = cardId)
    }

    suspend fun saveTagToCard(tagId: Long) {
        appRepository.upsertTagCardCrossRef(
            TagCardCrossRef(cardId = cardUiState.cardDetails.id, tagId = tagId)
        )
    }

    suspend fun deleteTagFromCard(tagId: Long) {
        appRepository.deleteTagCardCrossRef(
            cardId = cardUiState.cardDetails.id, tagId = tagId
        )
    }
}