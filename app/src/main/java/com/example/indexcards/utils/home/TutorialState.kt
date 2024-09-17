package com.example.indexcards.utils.home

sealed interface TutorialState {
    data object WELCOME : TutorialState
    data object ADD_BOX_INTRO : TutorialState
    data object ADD_BOX_DIALOG : TutorialState
    data object NEW_BOX : TutorialState
}

object TutorialMap {
    val map: Map<Int, TutorialState> = mapOf(
        0 to TutorialState.WELCOME,
        1 to TutorialState.ADD_BOX_INTRO,
        2 to TutorialState.ADD_BOX_DIALOG,
        3 to TutorialState.NEW_BOX,
    )
}