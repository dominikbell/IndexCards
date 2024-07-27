package com.example.indexcards.utils.box

import androidx.compose.ui.res.stringResource
import com.example.indexcards.R
import com.example.indexcards.data.Box
import com.example.indexcards.data.Card
import com.example.indexcards.data.CardWithTags
import com.example.indexcards.data.Tag
import com.example.indexcards.utils.state.emptyBox
import com.example.indexcards.utils.state.emptyTag


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

sealed interface BoxScreenSorting {
    data object DATE_ASC: BoxScreenSorting
    data object DATE_DESC: BoxScreenSorting
    data object WORD_ASC: BoxScreenSorting
    data object WORD_DESC: BoxScreenSorting
    data object LEVEL_ASC: BoxScreenSorting
    data object LEVEL_DESC: BoxScreenSorting
}

val boxScreenSorting: List<Pair<BoxScreenSorting, Int>> = listOf(
    Pair(BoxScreenSorting.DATE_ASC, R.string.date_asc),
    Pair(BoxScreenSorting.DATE_DESC, R.string.date_desc),
    Pair(BoxScreenSorting.WORD_ASC, R.string.word_asc),
    Pair(BoxScreenSorting.WORD_DESC, R.string.word_desc),
    Pair(BoxScreenSorting.LEVEL_ASC, R.string.level_asc),
    Pair(BoxScreenSorting.LEVEL_DESC, R.string.level_desc),
)