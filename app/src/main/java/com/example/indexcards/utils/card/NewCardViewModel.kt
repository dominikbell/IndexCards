package com.example.indexcards.utils.card

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import com.example.indexcards.data.AppRepository
import com.example.indexcards.data.TagCardCrossRef
import com.example.indexcards.utils.tag.emptyTag

class NewCardViewModel(
    appRepository: AppRepository,
    savedStateHandle: SavedStateHandle
) : EditCardViewModel(
    appRepository = appRepository,
    savedStateHandle = savedStateHandle
) {
    var tagList = mutableStateListOf(emptyTag)

    override suspend fun saveCard() {
        val newId = appRepository.getBiggestCardId() + 1

        updateUiState(cardUiState.cardDetails.copy(boxId = boxId, id = newId))

        if (validateInput(cardUiState.cardDetails)) {
            appRepository.upsertCard(cardUiState.cardDetails.toCard())
        }

        tagList.forEach {
            if (it != emptyTag) {
                appRepository.upsertTagCardCrossRef(
                    TagCardCrossRef(tagId = it.tagId, cardId = newId)
                )
            }
        }
    }
}