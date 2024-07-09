package com.example.indexcards.utils.box

import com.example.indexcards.data.Box
import com.example.indexcards.data.Card
import com.example.indexcards.data.CardWithTags
import com.example.indexcards.data.Tag
import com.example.indexcards.utils.tag.emptyTag


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
    data object VIEW: BoxScreenState
    data object EDIT: BoxScreenState
    data object TRAIN: BoxScreenState
}