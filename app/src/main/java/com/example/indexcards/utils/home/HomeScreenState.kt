package com.example.indexcards.utils.home

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
    data object MODIFIED_ASC: HomeScreenSorting
    data object MODIFIED_DESC: HomeScreenSorting
    data object CREATED_ASC: HomeScreenSorting
    data object CREATED_DESC: HomeScreenSorting
    data object NAME_ASC: HomeScreenSorting
    data object NAME_DESC: HomeScreenSorting
}