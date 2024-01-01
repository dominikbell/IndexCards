package com.example.indexcards.utils.box

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.indexcards.data.AppRepository
import com.example.indexcards.data.Box
import com.example.indexcards.data.Card
import com.example.indexcards.data.Tag
import com.example.indexcards.utils.tag.emptyTag
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class BoxScreenViewModel(
    private val appRepository: AppRepository,
    savedStateHandle: SavedStateHandle
) : BoxDetailViewModel(
    appRepository = appRepository,
    savedStateHandle = savedStateHandle
) {
    var tagWithCards: StateFlow<UiTagWithCards> = MutableStateFlow(UiTagWithCards())

    val boxWithCards: StateFlow<UiBoxWithCards> =
        appRepository.getBoxWithCardsStream(boxId = boxId)
            .filterNotNull()
            .map {
                UiBoxWithCards(
                    box = it.box,
                    cardList = it.cards,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = UiBoxWithCards()
            )
    suspend fun getTagWithCards(tagId: Long) {
        tagWithCards =
            appRepository.getTagWithCardsStream(tagId = tagId)
                .filterNotNull()
                .map {
                    UiTagWithCards(
                        tag = it.tag,
                        cardList = it.cards,
                    )
                }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                    initialValue = UiTagWithCards()
                )
    }

    fun resetTagWithCards() {
        tagWithCards = MutableStateFlow(UiTagWithCards())
    }
}

data class UiBoxWithCards(
    val box: Box = emptyBox,
    val cardList: List<Card> = listOf()
)

data class UiTagWithCards(
    val tag: Tag = emptyTag,
    val cardList: List<Card> = listOf()
)