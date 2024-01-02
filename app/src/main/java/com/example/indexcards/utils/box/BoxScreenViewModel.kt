package com.example.indexcards.utils.box

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.indexcards.data.AppRepository
import com.example.indexcards.data.Box
import com.example.indexcards.data.Card
import com.example.indexcards.data.Tag
import com.example.indexcards.data.TagWithCards
import com.example.indexcards.utils.tag.emptyTag
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class BoxScreenViewModel(
    private val appRepository: AppRepository,
    savedStateHandle: SavedStateHandle
) : BoxDetailViewModel(
    appRepository = appRepository,
    savedStateHandle = savedStateHandle
) {
    private val _tagSortedBy = MutableStateFlow(emptyTag)

    val tagWithCards: StateFlow<UiTagWithCards> = _tagSortedBy
        .flatMapLatest {
            when (it) {
                emptyTag -> flow {
                    emit(
                        TagWithCards(
                            tag = emptyTag,
                            cards = listOf()
                        )
                    )
                }

                else -> appRepository.getTagWithCardsStream(_tagSortedBy.value.tagId)
            }
        }
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

    fun setTagSortedBy(newTag: Tag) {
        _tagSortedBy.value = newTag
    }

    fun resetTagSortedBy() {
        _tagSortedBy.value = emptyTag
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