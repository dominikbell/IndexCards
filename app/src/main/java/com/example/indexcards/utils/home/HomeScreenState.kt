package com.example.indexcards.utils.home

import com.example.indexcards.R
import com.example.indexcards.data.Box


data class UiBoxList(
    val boxList: List<Box> = listOf()
)

sealed interface HomeScreenState {
    data object MAIN : HomeScreenState
    data object SETTINGS : HomeScreenState
    data object STATISTICS : HomeScreenState
}

sealed interface HomeScreenSorting {
    data object CREATED_ASC : HomeScreenSorting
    data object CREATED_DESC : HomeScreenSorting
    data object NAME_ASC : HomeScreenSorting
    data object NAME_DESC : HomeScreenSorting
    data object TOPIC : HomeScreenSorting
}

val homeScreenSorting: List<Pair<HomeScreenSorting, Int>> = listOf(
    Pair(HomeScreenSorting.CREATED_ASC, R.string.date_asc),
    Pair(HomeScreenSorting.CREATED_DESC, R.string.date_desc),
    Pair(HomeScreenSorting.NAME_ASC, R.string.name_asc),
    Pair(HomeScreenSorting.NAME_DESC, R.string.name_desc),
    Pair(HomeScreenSorting.TOPIC, R.string.topic),
)