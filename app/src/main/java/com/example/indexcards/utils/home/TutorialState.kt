package com.example.indexcards.utils.home

sealed interface TutorialState {
    data object ERROR : TutorialState
    data object OFF : TutorialState
    data object FIRST_START : TutorialState
    data object WELCOME : TutorialState
    data object HIGHLIGHT_MENU : TutorialState
    data object ADD_BOX_INTRO : TutorialState
    data object ADD_BOX_DIALOG : TutorialState
    data object ADD_BOX_DIALOG_NAME : TutorialState
    data object ADD_BOX_DIALOG_TOPIC : TutorialState
    data object ADD_BOX_DIALOG_CHECK_BOX : TutorialState
    data object ADD_BOX_DIALOG_DESCRIPTION : TutorialState
    data object ADD_BOX_DIALOG_REMINDER : TutorialState
    data object ADD_BOX_DIALOG_SAVE : TutorialState
    data object NEW_BOX : TutorialState
}

object TutorialMap {
    val map: Map<Int, TutorialState> = mapOf(
        -1 to TutorialState.OFF,
        0 to TutorialState.FIRST_START,
        1 to TutorialState.WELCOME,
//        2 to TutorialState.HIGHLIGHT_MENU,
        2 to TutorialState.ADD_BOX_INTRO,
        3 to TutorialState.ADD_BOX_INTRO,
        4 to TutorialState.ADD_BOX_DIALOG,
        5 to TutorialState.ADD_BOX_DIALOG_NAME,
        6 to TutorialState.ADD_BOX_DIALOG_TOPIC,
        7 to TutorialState.ADD_BOX_DIALOG_CHECK_BOX,
        8 to TutorialState.ADD_BOX_DIALOG_DESCRIPTION,
        9 to TutorialState.ADD_BOX_DIALOG_REMINDER,
        10 to TutorialState.ADD_BOX_DIALOG_SAVE,
        11 to TutorialState.NEW_BOX,
    )
}