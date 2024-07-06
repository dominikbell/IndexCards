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