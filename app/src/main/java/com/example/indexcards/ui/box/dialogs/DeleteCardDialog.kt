package com.example.indexcards.ui.box.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.indexcards.R
import com.example.indexcards.data.Card
import com.example.indexcards.utils.state.emptyCard


@Composable
fun DeleteCardDialog(
    modifier: Modifier = Modifier,
    currentCard: Card,
    onDismiss: () -> Unit = { },
    deleteCard: () -> Unit = { },
) {
    AlertDialog(
        modifier = modifier,
        title = { Text(text = stringResource(R.string.delete_card) + " '${currentCard.word}'") },
        text = { Text(text = stringResource(R.string.delete_card_sure)) },
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

@Composable
fun DeleteCardsDialog(
    modifier: Modifier = Modifier,
    cardList: List<Card>,
    onDismiss: () -> Unit = { },
    deleteCards: () -> Unit = { },
) {
    AlertDialog(
        modifier = modifier,
        title = { Text(text = stringResource(R.string.delete_cards)) },
        text = {
            Column(modifier = modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.padding(bottom = 2.dp),
                    text = stringResource(id = R.string.delete_cards_before),
                )
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    for (card in cardList) {
                        Text(
                            text = "\u2022 " + card.word,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 8.dp),
                    text = stringResource(id = R.string.action_cannot_be_undone),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(text = stringResource(R.string.delete_cards_sure))
            }
        },
        onDismissRequest = onDismiss,
        confirmButton =
            {
                TextButton(
                    onClick = {
                        deleteCards()
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
fun DeleteCardsPreview() {
    DeleteCardsDialog(
        modifier = Modifier,
        cardList = listOf(
            emptyCard.copy(word = "Karte123"),
            emptyCard.copy(word = "Karte456"),
            emptyCard.copy(word = "Karte897"),
        ),
    )
}