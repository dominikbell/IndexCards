package com.example.indexcards.utils.card

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.indexcards.data.AppRepository
import com.example.indexcards.data.Card
import com.example.indexcards.data.CardWithTags
import com.example.indexcards.data.Tag
import com.example.indexcards.utils.box.BoxDetailViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
open class CardViewModel(
    appRepository: AppRepository,
    savedStateHandle: SavedStateHandle
) : BoxDetailViewModel(
    appRepository = appRepository,
    savedStateHandle = savedStateHandle
) {
    val currentCard = MutableStateFlow(emptyCard)

    var cardUiState by mutableStateOf(CardState())

    val cardWithTags: StateFlow<UiCardWithTags> = currentCard
        .flatMapLatest {
            when (it) {
                emptyCard -> flow {
                    emit(
                        CardWithTags(
                            card = emptyCard,
                            tags = listOf()
                        )
                    )
                }

                else -> appRepository.getCardWithTagsStream(currentCard.value.cardId)
            }
        }
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
        currentCard.value = appRepository.getCard(cardId)
            .filterNotNull()
            .first()
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