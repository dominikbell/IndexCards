package com.example.indexcards.ui.box.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.indexcards.R
import com.example.indexcards.data.Tag
import com.example.indexcards.ui.elements.MeaningField
import com.example.indexcards.ui.elements.NewTagButton
import com.example.indexcards.ui.elements.NotesField
import com.example.indexcards.ui.elements.RequiredFieldsText
import com.example.indexcards.ui.elements.WordField
import com.example.indexcards.ui.box.TagList
import com.example.indexcards.utils.box.UiBoxWithTags
import com.example.indexcards.utils.box.emptyBox
import com.example.indexcards.utils.card.CardDetails
import com.example.indexcards.utils.card.CardState
import com.example.indexcards.utils.card.UiCardWithTags
import com.example.indexcards.utils.card.emptyCard
import com.example.indexcards.utils.card.toCardDetails
import com.example.indexcards.utils.tag.emptyTag

@Composable
fun NewCardDialog(
    modifier: Modifier = Modifier,
    cardUiState: CardState,
    boxWithTags: UiBoxWithTags,
    updateUiState: (CardDetails) -> Unit = {},
    onDismiss: () -> Unit = {},
    saveCard: () -> Unit = {},
    onTagClick: (Tag) -> Unit = {},
    showNewTagDialog: () -> Unit = {},
    showEditTagDialog: (Tag) -> Unit = {},
) {
    CardDialogBody(
        titleText = stringResource(id = R.string.add_new_card),
        cardUiState = cardUiState,
        boxWithTags = boxWithTags,
        onDismiss = onDismiss,
        onSave = saveCard,
        deleteButton = false,
        onDelete = {},
        updateUiState = updateUiState,
        onTagClick = onTagClick,
        showNewTagDialog = showNewTagDialog,
        showEditTagDialog = showEditTagDialog,
    )
}

@Preview
@Composable
fun NewCardDialogPreview() {
    NewCardDialog(
        cardUiState = CardState(
            cardDetails = emptyCard.copy(
                word = "New Card",
                meaning = "Bedeutung12",
                notes = "Notizen dazu"
            ).toCardDetails()
        ),
        boxWithTags = UiBoxWithTags(
            box = emptyBox.copy(name = "Box123"),
            tagList = listOf(
                emptyTag.copy(tagId = 1, text = "Tag1"),
                emptyTag.copy(tagId = 2, text = "Tag2"),
            )
        ),
    )
}

@Composable
fun EditCardDialog(
    modifier: Modifier = Modifier,
    boxWithTags: UiBoxWithTags,
    cardWithTags: UiCardWithTags,
    cardUiState: CardState,
    onDismiss: () -> Unit = {},
    onDeleteCard: () -> Unit = {},
    showNewTagDialog: () -> Unit = {},
    showEditTagDialog: (Tag) -> Unit = {},
    updateUiState: (CardDetails) -> Unit = {},
    saveCard: () -> Unit = {},
    clickOnTag: (Tag) -> Unit = {},
) {
    val titleText = stringResource(id = R.string.edit_card) + " " + cardWithTags.card.word

    CardDialogBody(
        titleText = titleText,
        cardUiState = cardUiState,
        boxWithTags = boxWithTags,
        deleteButton = true,
        onDismiss = onDismiss,
        onSave = saveCard,
        onDelete = onDeleteCard,
        updateUiState = updateUiState,
        onTagClick = clickOnTag,
        showNewTagDialog = showNewTagDialog,
        showEditTagDialog = { showEditTagDialog(it) },
    )
}

@Preview
@Composable
fun EditCardDialogPreview() {
    EditCardDialog(
        cardUiState = CardState(
            cardDetails = emptyCard.copy(
                word = "New Name",
                meaning = "Bedeutung12",
                notes = "Notizen dazu"
            ).toCardDetails()
        ),
        boxWithTags = UiBoxWithTags(
            box = emptyBox.copy(name = "Box123"),
            tagList = listOf(
                emptyTag.copy(tagId = 1, text = "Tag1"),
                emptyTag.copy(tagId = 2, text = "Tag2"),
            )
        ),
        cardWithTags = UiCardWithTags(
            card = emptyCard.copy(word = "OldName")
        )
    )
}

@Composable
fun CardDialogBody(
    modifier: Modifier = Modifier,
    titleText: String,
    cardUiState: CardState,
    boxWithTags: UiBoxWithTags,
    deleteButton: Boolean,
    onDismiss: () -> Unit = {},
    onSave: () -> Unit = {},
    onDelete: () -> Unit = {},
    updateUiState: (CardDetails) -> Unit = {},
    onTagClick: (Tag) -> Unit = {},
    showNewTagDialog: () -> Unit = {},
    showEditTagDialog: (Tag) -> Unit = {},
) {
    AlertDialog(modifier = modifier,
        onDismissRequest = onDismiss,
        title = { Text(text = titleText) },
        text = {
            Column(
                modifier = modifier
            ) {
                WordField(
                    cardUiState = cardUiState,
                    onValueChange = { updateUiState(cardUiState.cardDetails.copy(word = it)) }
                )

                MeaningField(
                    cardUiState = cardUiState,
                    onValueChange = { updateUiState(cardUiState.cardDetails.copy(meaning = it)) }
                )

                RequiredFieldsText()

                Row(
                    modifier = modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TagList(
                        modifier = modifier.weight(1f),
                        tagList = boxWithTags.tagList,
                        onClick = { onTagClick(it) },
                        onLongClick = { showEditTagDialog(it) },
                        selectedTags = cardUiState.tagList
                    )

                    VerticalDivider(
                        modifier = Modifier
                            .height(16.dp)
                            .padding(start = 3.dp, end = 3.dp)
                    )

                    NewTagButton(
                        onClick = showNewTagDialog,
                        short = true
                    )
                }

                NotesField(
                    cardUiState = cardUiState,
                    onValueChange = { updateUiState(cardUiState.cardDetails.copy(notes = it)) }
                )
            }
        },

        confirmButton = {
            TextButton(
                onClick = onSave
            ) {
                Text(text = stringResource(R.string.save))
            }
        },

        dismissButton = {
            Row {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text(text = stringResource(R.string.cancel))
                }
                if (deleteButton) {
                    TextButton(
                        onClick = onDelete
                    ) {
                        Text(text = stringResource(R.string.delete))
                    }
                }
            }
        }
    )
}