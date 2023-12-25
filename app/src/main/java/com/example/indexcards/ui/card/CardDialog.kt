package com.example.indexcards.ui.card

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.ui.box.MeaningField
import com.example.indexcards.ui.box.NotesField
import com.example.indexcards.ui.box.WordField
import com.example.indexcards.utils.AppViewModelProvider
import com.example.indexcards.utils.card.EditCardViewModel
import kotlinx.coroutines.launch

@Composable
fun CardDialog(
    modifier: Modifier = Modifier,
    hideDialog: () -> Unit,
    newCard: Boolean,
    editCardViewModel: EditCardViewModel = viewModel(
        factory = AppViewModelProvider(context = LocalContext.current).factory
    )
) {
    val newCardUiState = editCardViewModel.cardUiState
    val titleText = if (newCard) {
         "Add a new card"
    } else {
        "Edit card"
    }

    fun onDismiss() {
        hideDialog()
        editCardViewModel.resetUiStatus()
    }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = { onDismiss() },
        title = { Text(text = titleText) },
        text = {
            Column(
                modifier = modifier
            ) {
                WordField(
                    cardUiState = newCardUiState,
                    onValueChange = {
                        editCardViewModel.updateUiState(
                            newCardUiState.cardDetails.copy(word = it)
                        )
                    }
                )

                MeaningField(
                    cardUiState = newCardUiState,
                    onValueChange = {
                        editCardViewModel.updateUiState(
                            newCardUiState.cardDetails.copy(meaning = it)
                        )
                    }
                )

                NotesField(
                    cardUiState = newCardUiState,
                    onValueChange = {
                        editCardViewModel.updateUiState(
                            newCardUiState.cardDetails.copy(notes = it)
                        )
                    }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    editCardViewModel.viewModelScope.launch {
                        editCardViewModel.saveCard()
                    }
                    onDismiss()
                }
            ) {
                Text(text = "Save")
            }
        },

        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = "Cancel")
            }
        }
    )
}