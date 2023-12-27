package com.example.indexcards.ui.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.ui.home.MeaningField
import com.example.indexcards.ui.home.NewTagButton
import com.example.indexcards.ui.home.NotesField
import com.example.indexcards.ui.home.WordField
import com.example.indexcards.ui.tag.TagDialog
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
    var tagDialog by remember { mutableStateOf(false) }
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

                LazyRow(
                    modifier = Modifier,
                ) {
                    /* TODO: list used tags here */
                }

                LazyRow(
                    modifier = Modifier,
                ) {
                    /* TODO: list unused tags here */
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    NewTagButton(onClick = { tagDialog = true })
                }

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

    if (tagDialog) {
        TagDialog(
            modifier = modifier,
            hideDialog = { tagDialog = false }
        )
    }
}