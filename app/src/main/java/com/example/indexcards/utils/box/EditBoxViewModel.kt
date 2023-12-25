package com.example.indexcards.utils.box

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.indexcards.data.AppRepository
import com.example.indexcards.data.Card
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EditBoxViewModel(
    private val appRepository: AppRepository,
    savedStateHandle: SavedStateHandle
) : BoxViewModel(
    appRepository = appRepository
) {
    private val boxId: Long = checkNotNull(savedStateHandle["boxId"])

    var newBoxState by mutableStateOf(BoxState())

    val numberOfCards: StateFlow<Int> =
        appRepository.getNumberOfCards(boxId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = 0
        )

    val boxWithCards: StateFlow<CardList> =
        appRepository.getBoxWithCardsStream(boxId = boxId).map {
            CardList(
                it.cards
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = CardList()
        )

    init {
        viewModelScope.launch {
            boxUiState = appRepository.getBoxStream(boxId)
                .filterNotNull()
                .first()
                .toBoxState(true)

            newBoxState = BoxState(
                boxDetails = boxUiState.boxDetails.copy(),
                isValid = true
            )
        }
    }

    fun updateNewBoxState(boxDetails: BoxDetails) {
        newBoxState =
            BoxState(
                boxDetails = boxDetails,
                isValid = validateInput(boxDetails)
            )
    }

    suspend fun saveEdit() {
        copyNewStateToBox()
        appRepository.updateBox(boxUiState.boxDetails.toBox())
    }

    private fun copyNewStateToBox() {
        boxUiState = newBoxState
    }

    suspend fun deleteBox(boxId: Long) {
        appRepository.deleteBox(boxId)
    }
}

data class CardList(
    val cardList: List<Card> = listOf()
)