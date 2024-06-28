package com.example.indexcards.utils.box

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
    val tagSortedBy = MutableStateFlow<Tag>(emptyTag)
    val boxId: Long = checkNotNull(savedStateHandle["boxId"])
    val levelSelected = MutableStateFlow(-1)

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
}

data class UiBoxWithCards(
    val box: Box = emptyBox,
    val cardList: List<Card> = listOf()
)

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
