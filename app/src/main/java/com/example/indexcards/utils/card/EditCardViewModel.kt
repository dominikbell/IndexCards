package com.example.indexcards.utils.card

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.indexcards.data.AppRepository
import com.example.indexcards.data.CardWithTags
import com.example.indexcards.data.Tag
import com.example.indexcards.utils.box.BoxViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class EditCardViewModel(
    private val appRepository: AppRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val boxId: Long = checkNotNull(savedStateHandle["boxId"])
//    private val cardId: Long = checkNotNull(savedStateHandle["cardId"])

    var cardUiState by mutableStateOf(CardState())
    var newTagList by mutableStateOf(UiTagList())

    val cardWithTags: StateFlow<UiTagList> =
        appRepository.getCardWithTagsStream(cardId = cardUiState.cardDetails.id).map {
            UiTagList(it.tags)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(BoxViewModel.TIMEOUT_MILLIS),
            initialValue = UiTagList()
        )

    suspend fun saveCard() {
        updateUiState(cardUiState.cardDetails.copy(boxId = boxId))
        if (validateInput(cardUiState.cardDetails)) {
            appRepository.insertCard(cardUiState.cardDetails.toCard())
        }
    }

    suspend fun saveTagsToCard() {}

    fun addTagToList(tag: Tag) {
        newTagList = UiTagList(newTagList.tagList + listOf(tag))
    }

    fun removeFromTagToList(tag: Tag) {
        if (newTagList.tagList.contains(tag)) {
            newTagList = UiTagList(newTagList.tagList - listOf(tag).toSet())
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

    private fun validateInput(
        uiState: CardDetails = cardUiState.cardDetails
    ): Boolean {
        return with(uiState) {
            word.isNotBlank() && meaning.isNotBlank()
        }
    }

    companion object {
        const val TIMEOUT_MILLIS = 5_000L
    }
}

data class UiTagList(
    val tagList: List<Tag> = listOf()
)