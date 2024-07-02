package com.example.indexcards.utils.box

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.indexcards.data.AppRepository
import com.example.indexcards.data.Box
import com.example.indexcards.data.Card
import com.example.indexcards.data.CardWithTags
import com.example.indexcards.data.Tag
import com.example.indexcards.data.TagWithCards
import com.example.indexcards.utils.tag.emptyTag
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class BoxScreenViewModel(
    appRepository: AppRepository,
    savedStateHandle: SavedStateHandle
) : BoxViewModel(
    appRepository = appRepository,
) {
    val boxId: Long = checkNotNull(savedStateHandle["boxId"])
    val tagSortedBy = MutableStateFlow(emptyTag)
    val levelSelected = MutableStateFlow(-1)
    val trainingCounts = MutableStateFlow(false)
    var boxScreenState: BoxScreenState by mutableStateOf(BoxScreenState.VIEW)

    val boxWithTags: StateFlow<UiBoxWithTags> =
        appRepository.getBoxWithTagsStream(boxId = boxId)
            .filterNotNull()
            .map {
                UiBoxWithTags(
                    box = it.box,
                    tagList = it.tags,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = UiBoxWithTags()
            )

    val tagWithCards: StateFlow<UiTagWithCards> = tagSortedBy
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

                else -> appRepository.getTagWithCardsStream(tagSortedBy.value.tagId)
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

    val cardsWithTags: StateFlow<UiCardsWithTags> =
        appRepository.getAllCardsWithTagsOfBoxStream(boxId = boxId)
            .filterNotNull()
            .map {
                UiCardsWithTags(it)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = UiCardsWithTags()
            )

    fun changeBoxScreenState(newState: BoxScreenState) {
        boxScreenState = newState
    }

    fun setTagSortedBy(newTag: Tag) {
        tagSortedBy.update {
            newTag
        }
    }

    fun resetTagSortedBy() {
        tagSortedBy.update {
            emptyTag
        }
    }

    fun deleteBox() {
        viewModelScope.launch {
            appRepository.deleteBox(boxId = boxId)
        }
    }

    fun updateSelectedLevel(newLevel: Int) {
        if (newLevel == levelSelected.value) {
            levelSelected.update { -1 }
        } else {
            levelSelected.update { newLevel }
        }
    }

    fun changeTrainingCounts() {
        trainingCounts.update { !trainingCounts.value }
    }

    suspend fun onCardCorrect(card: Card) {
        if (card.level < 4) {
            appRepository.upgradeLevelOnCard(card.cardId)
        }
    }

    suspend fun onCardIncorrect(card: Card) {
        if (card.level > 0) {
            appRepository.downgradeLevelOnCard(card.cardId)
        }
    }
}

data class UiTagWithCards(
    val tag: Tag = emptyTag,
    val cardList: List<Card> = listOf()
)

data class UiBoxWithTags(
    val box: Box = emptyBox,
    val tagList: List<Tag> = listOf()
)

data class UiCardsWithTags(
    val cardWithTagList: List<CardWithTags> = listOf()
)

sealed interface BoxScreenState {
    object VIEW: BoxScreenState
    object EDIT: BoxScreenState
    object TRAIN: BoxScreenState
}