package com.example.indexcards.ui.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.ui.home.MeaningField
import com.example.indexcards.ui.home.NewTagButton
import com.example.indexcards.ui.home.NotesField
import com.example.indexcards.ui.home.WordField
import com.example.indexcards.ui.tag.TagList
import com.example.indexcards.ui.tag.TagListItem
import com.example.indexcards.utils.ViewModelProvider
import com.example.indexcards.utils.box.UiBoxWithTags
import com.example.indexcards.utils.card.EditCardViewModel
import com.example.indexcards.utils.tag.EditTagViewModel
import kotlinx.coroutines.launch

@Composable
fun EditCardDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    newCard: Boolean,
    showDeleteCard: () -> Unit,
    boxWithTags: UiBoxWithTags,
    showNewTagDialog: () -> Unit,
    showEditTagDialog: () -> Unit,
    editCardViewModel: EditCardViewModel = viewModel(
        factory = ViewModelProvider(context = LocalContext.current).factory
    ),
    editTagViewModel: EditTagViewModel = viewModel(
        factory = ViewModelProvider(context = LocalContext.current).factory
    )
) {
    val cardUiState = editCardViewModel.cardUiState
    val cardWithTags = editCardViewModel.cardWithTags.collectAsState()

    val titleText = if (newCard) {
        "Add a new card"
    } else {
        "Edit card"
    }

    fun newOnDismiss() {
        onDismiss()
        editCardViewModel.resetUiStatus()
    }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = { newOnDismiss() },
        title = { Text(text = titleText) },
        text = {
            Column(
                modifier = modifier
            ) {
                WordField(
                    cardUiState = cardUiState,
                    onValueChange = {
                        editCardViewModel.updateUiState(
                            cardUiState.cardDetails.copy(word = it)
                        )
                    }
                )

                MeaningField(
                    cardUiState = cardUiState,
                    onValueChange = {
                        editCardViewModel.updateUiState(
                            cardUiState.cardDetails.copy(meaning = it)
                        )
                    }
                )

                if (cardWithTags.value.tagList.isEmpty()) {
                    Text(text = "No tags for this card", modifier = Modifier.padding(4.dp))
                } else {
                    LazyRow(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        /* TODO: list used tags here */
                        items(
                            items = cardWithTags.value.tagList,
                            key = { it.tagId }
                        ) { item ->
                            TagListItem(
                                item = item,
                                onClick = { },
                                onLongClick = { }
                            )
                        }
                    }
                }

                TagList(
                    tagList = cardWithTags.value.tagList,
                    onLongClick = { showEditTagDialog() }
                )

                Row(modifier = Modifier.fillMaxWidth()) {
                    NewTagButton(onClick = { })
                }

                NotesField(
                    cardUiState = cardUiState,
                    onValueChange = {
                        editCardViewModel.updateUiState(
                            cardUiState.cardDetails.copy(notes = it)
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
                    newOnDismiss()
                }
            ) {
                Text(text = "Save")
            }
        },

        dismissButton = {
            Row() {
                TextButton(
                    onClick = {
                        if (!newCard) {
//                        editCardViewModel.viewModelScope.launch {
//                            editCardViewModel.deleteTagsFromCard()
//                        }
                        }
                        newOnDismiss()
                    }) {
                    Text(text = "Cancel")
                }
                if (!newCard) {
                    TextButton(
                        onClick = {
                            editCardViewModel.viewModelScope.launch {
                                editCardViewModel.setCurrentCard(cardUiState.cardDetails.id)
                            }
                            showDeleteCard()
                            newOnDismiss()
                        }) {
                        Text(text = "Delete")
                    }
                }
            }
        }
    )
}