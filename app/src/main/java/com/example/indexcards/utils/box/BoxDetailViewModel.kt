package com.example.indexcards.utils.box

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.indexcards.data.AppRepository
import com.example.indexcards.utils.AppViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

open class BoxDetailViewModel(
    appRepository: AppRepository,
    savedStateHandle: SavedStateHandle
) : AppViewModel(
    appRepository = appRepository,
) {
    val boxId: Long = checkNotNull(savedStateHandle["boxId"])

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
}
