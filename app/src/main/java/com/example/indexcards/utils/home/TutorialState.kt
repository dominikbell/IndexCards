package com.example.indexcards.utils.home

sealed interface TutorialState {
    data object ERROR : TutorialState
    data object OFF : TutorialState
    data object FIRST_START : TutorialState

    data object WELCOME : TutorialState
    data object ADD_BOX_INTRO : TutorialState
    data object ADD_BOX_DIALOG : TutorialState
    data object ADD_BOX_DIALOG_NAME : TutorialState
    data object ADD_BOX_DIALOG_TOPIC : TutorialState
    data object ADD_BOX_DIALOG_CHECK_BOX : TutorialState
    data object ADD_BOX_DIALOG_DESCRIPTION : TutorialState
    data object ADD_BOX_DIALOG_REMINDER : TutorialState
    data object ADD_BOX_DIALOG_SAVE : TutorialState
    data object NEW_BOX : TutorialState

    data object ADD_CARD_INTRO : TutorialState
    data object ADD_CARD_DIALOG : TutorialState
    data object ADD_CARD_DIALOG_WORD : TutorialState
    data object ADD_CARD_DIALOG_MEANING : TutorialState
    data object ADD_CARD_DIALOG_CATEGORY : TutorialState
    data object ADD_CARD_DIALOG_TAG_LIST : TutorialState
    data object ADD_CARD_DIALOG_MEMO : TutorialState
    data object ADD_CARD_DIALOG_NOTES : TutorialState
    data object ADD_CARD_DIALOG_SAVE : TutorialState

    data object HIGHLIGHT_MENU : TutorialState
}

fun TutorialState.isEqualOrLaterThan(otherState: TutorialState): Boolean {
    val thisInd = TutorialMap.map.entries.firstOrNull { it.value == this }?.key ?: -1
    val otherInd = TutorialMap.map.entries.firstOrNull { it.value == otherState }?.key ?: -1

    return thisInd >= otherInd
}

object TutorialMap {
    val map: Map<Int, TutorialState> = mapOf(
        -1 to TutorialState.OFF,
        0 to TutorialState.FIRST_START,

        1 to TutorialState.WELCOME,
        2 to TutorialState.ADD_BOX_INTRO,
        3 to TutorialState.ADD_BOX_DIALOG,
        4 to TutorialState.ADD_BOX_DIALOG_NAME,
        5 to TutorialState.ADD_BOX_DIALOG_TOPIC,
        6 to TutorialState.ADD_BOX_DIALOG_CHECK_BOX,
        7 to TutorialState.ADD_BOX_DIALOG_DESCRIPTION,
        8 to TutorialState.ADD_BOX_DIALOG_REMINDER,
        9 to TutorialState.ADD_BOX_DIALOG_SAVE,
        10 to TutorialState.NEW_BOX,

        11 to TutorialState.ADD_CARD_INTRO,
        12 to TutorialState.ADD_CARD_DIALOG,
        13 to TutorialState.ADD_CARD_DIALOG_WORD,
        14 to TutorialState.ADD_CARD_DIALOG_MEANING,
        15 to TutorialState.ADD_CARD_DIALOG_CATEGORY,
        16 to TutorialState.ADD_CARD_DIALOG_TAG_LIST,
        17 to TutorialState.ADD_CARD_DIALOG_MEMO,
        18 to TutorialState.ADD_CARD_DIALOG_NOTES,
        19 to TutorialState.ADD_CARD_DIALOG_SAVE,
    )
}