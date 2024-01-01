package com.example.indexcards.utils.card

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.indexcards.data.AppRepository
import com.example.indexcards.data.Card
import com.example.indexcards.data.Tag
import com.example.indexcards.utils.box.BoxDetailViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

open class CardViewModel(
    private val appRepository: AppRepository,
    savedStateHandle: SavedStateHandle
) : BoxDetailViewModel(
    appRepository = appRepository,
    savedStateHandle = savedStateHandle
) {
    var currentCard by mutableStateOf(emptyCard)
    var cardUiState by mutableStateOf(CardState())

    val cardWithTags: StateFlow<UiCardWithTags> =
        appRepository.getCardWithTagsStream(cardId = cardUiState.cardDetails.id)
            .filterNotNull()
            .map {
                UiCardWithTags(
                    card = it.card,
                    tagList = it.tags,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = UiCardWithTags()
            )


    suspend fun setCurrentCard(cardId: Long) {
        viewModelScope.launch {
            currentCard = appRepository.getCard(cardId)
                .filterNotNull()
                .first()

        }
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

    fun validateInput(
        uiState: CardDetails = cardUiState.cardDetails
    ): Boolean {
        return with(uiState) {
            word.isNotBlank() && meaning.isNotBlank()
        }
    }
}

data class UiCardWithTags(
    val card: Card = emptyCard,
    val tagList: List<Tag> = listOf()
)