package com.example.indexcards.ui.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.data.Tag
import com.example.indexcards.ui.home.MeaningField
import com.example.indexcards.ui.home.NewTagButton
import com.example.indexcards.ui.home.NotesField
import com.example.indexcards.ui.home.WordField
import com.example.indexcards.ui.tag.TagList
import com.example.indexcards.utils.ViewModelProvider
import com.example.indexcards.utils.box.UiBoxWithTags
import com.example.indexcards.utils.card.CardDetails
import com.example.indexcards.utils.card.CardState
import com.example.indexcards.utils.card.CardViewModel
import com.example.indexcards.utils.card.EditCardViewModel
import com.example.indexcards.utils.card.NewCardViewModel
import kotlinx.coroutines.launch

@Composable
fun EditCardDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    showCardDialog: () -> Unit,
    showDeleteCard: () -> Unit,
    boxWithTags: UiBoxWithTags,
    showNewTagDialog: () -> Unit,
    showEditTagDialog: () -> Unit,
    cardViewModel: CardViewModel = viewModel(
        factory = ViewModelProvider(context = LocalContext.current).factory
    ),
    editCardViewModel: EditCardViewModel = viewModel(
        factory = ViewModelProvider(context = LocalContext.current).factory
    ),
) {
    val cardWithTags = editCardViewModel.cardWithTags.collectAsState()
    val currentCard = editCardViewModel.currentCard.collectAsState()

    CardDialogBody(
        modifier = modifier,
        onDismiss = onDismiss,
        onDelete = showDeleteCard,
        onSave = {
            editCardViewModel.viewModelScope.launch {
                editCardViewModel.saveCard()
            }
            cardViewModel.updateUiState(editCardViewModel.cardUiState.cardDetails)
            showCardDialog()
        },
        titleText = "Edit Card for '${currentCard.value.word}'",
        deleteButton = true,
        cardUiState = editCardViewModel.cardUiState,
        onWordChange = {
            editCardViewModel.updateUiState(
                editCardViewModel.cardUiState.cardDetails.copy(word = it)
            )
        },
        onMeaningChange = {
            editCardViewModel.updateUiState(
                editCardViewModel.cardUiState.cardDetails.copy(meaning = it)
            )
        },
        onNotesChange = {
            editCardViewModel.updateUiState(
                editCardViewModel.cardUiState.cardDetails.copy(notes = it)
            )
        },
        boxWithTags = boxWithTags,
        tagList = cardWithTags.value.tagList,
        onTagClick = {
            editCardViewModel.viewModelScope.launch {
                if (cardWithTags.value.tagList.contains(it)) {
                    editCardViewModel.deleteTagFromCard(it.tagId)
                } else {
                    editCardViewModel.saveTagToCard(it.tagId)
                }
            }
        },
        showNewTagDialog = showNewTagDialog,
        showEditTagDialog = showEditTagDialog,
    )
}

@Composable
fun NewCardDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    boxWithTags: UiBoxWithTags,
    showNewTagDialog: () -> Unit,
    showEditTagDialog: () -> Unit,
    newCardViewModel: NewCardViewModel = viewModel(
        factory = ViewModelProvider(context = LocalContext.current).factory
    ),
) {
    CardDialogBody(
        modifier = modifier,
        onDismiss = onDismiss,
        onDelete = {},
        onSave = {
            newCardViewModel.viewModelScope.launch {
                newCardViewModel.saveCard()
                newCardViewModel.updateUiState(CardDetails())
            }
            onDismiss()
        },
        titleText = "Add a new card",
        deleteButton = false,
        cardUiState = newCardViewModel.cardUiState,
        onWordChange = {
            newCardViewModel.updateUiState(
                newCardViewModel.cardUiState.cardDetails.copy(word = it)
            )
        },
        onMeaningChange = {
            newCardViewModel.updateUiState(
                newCardViewModel.cardUiState.cardDetails.copy(meaning = it)
            )
        },
        onNotesChange = {
            newCardViewModel.updateUiState(
                newCardViewModel.cardUiState.cardDetails.copy(notes = it)
            )
        },
        boxWithTags = boxWithTags,
        tagList = newCardViewModel.tagList,
        onTagClick = { newCardViewModel.tagList += it },
        showNewTagDialog = showNewTagDialog,
        showEditTagDialog = showEditTagDialog,
    )
}

@Composable
fun CardDialogBody(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onSave: () -> Unit,
    titleText: String,
    deleteButton: Boolean,
    cardUiState: CardState,
    onWordChange: (String) -> Unit,
    onMeaningChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    boxWithTags: UiBoxWithTags,
    tagList: List<Tag>,
    onTagClick: (Tag) -> Unit,
    showNewTagDialog: () -> Unit,
    showEditTagDialog: () -> Unit,
) {
    AlertDialog(modifier = modifier,
        onDismissRequest = onDismiss,
        title = { Text(text = titleText) },
        text = {
            Column(
                modifier = modifier
            ) {
                WordField(
                    cardUiState = cardUiState, onValueChange = onWordChange
                )

                MeaningField(
                    cardUiState = cardUiState, onValueChange = onMeaningChange
                )

                Row(
                    modifier = modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TagList(
                        modifier = modifier.weight(1f),
                        tagList = boxWithTags.tagList,
                        onClick = { onTagClick(it) },
                        onLongClick = { showEditTagDialog() },
                        selectedTags = tagList
                    )
                    NewTagButton(onClick = showNewTagDialog)
                }

                NotesField(cardUiState = cardUiState, onValueChange = { onNotesChange(it) })
            }
        },

        confirmButton = {
            TextButton(
                onClick = onSave
            ) {
                Text(text = "Save")
            }
        },

        dismissButton = {
            Row {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text(text = "Cancel")
                }
                if (deleteButton) {
                    TextButton(
                        onClick = onDelete
                    ) {
                        Text(text = "Delete")
                    }
                }
            }
        }
    )
}