package com.example.indexcards.ui.box.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.indexcards.R
import com.example.indexcards.data.Card
import com.example.indexcards.utils.state.emptyCard

@Composable
fun DeleteCardDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = { },
    deleteCard: () -> Unit = { },
    currentCard: Card,
) {
    AlertDialog(
        modifier = modifier,
        text = { Text(text = stringResource(R.string.delete_card_sure)) },
        title = { Text(text = stringResource(R.string.delete_card) + " '${currentCard.word}'") },
        onDismissRequest = onDismiss,
        confirmButton =
        {
            TextButton(
                onClick = {
                    deleteCard()
                    onDismiss()
                }
            ) {
                Text(text = stringResource(R.string.delete))
            }
        },
        dismissButton =
        {
            TextButton(
                onClick = onDismiss
            ) {
                Text(text = stringResource(R.string.cancel))
            }
        },
    )
}

@Preview
@Composable
fun DeleteCardDialogPreview() {
    DeleteCardDialog(
        currentCard = emptyCard.copy(word = "Karte123")
    )
}