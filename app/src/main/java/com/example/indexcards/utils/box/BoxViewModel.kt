package com.example.indexcards.utils.box

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BoxViewModel : ViewModel() {

    private val _state = MutableStateFlow(BoxState())
    val state: StateFlow<BoxState> = _state.asStateFlow()

    var userInput by mutableStateOf("")

    fun setName(newName: String) {
        _state.update { currentState ->
            currentState.copy(name = newName)
        }
    }

    fun setTopic(newTopic: String) {
        _state.update { currentState ->
            currentState.copy(topic = newTopic)
        }
    }

    fun setDescription(newDescription: String) {
        _state.update { currentState ->
            currentState.copy(description = newDescription)
        }
    }
}