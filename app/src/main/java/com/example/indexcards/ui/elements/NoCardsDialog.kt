package com.example.indexcards.ui.elements

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.indexcards.R

@Composable
fun NoCardsDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    level: Int
) {
    AlertDialog(
        modifier = modifier,
        title = { Text(text = stringResource(id = R.string.oops_no_cards)) },
        text = {
            Text(
                text = stringResource(id = R.string.no_cards_of_level_1) +
                        " " + (level + 1).toString() +
                        stringResource(id = R.string.no_cards_of_level_2)
            )
        },
        onDismissRequest = onDismiss,
        confirmButton = { },
        dismissButton =
        {
            TextButton(
                onClick = onDismiss
            ) {
                Text(text = stringResource(id = R.string.back_to_box))
            }
        },
    )
}

@Preview
@Composable
fun NoCardsDialogPreview() {
    NoCardsDialog(
        onDismiss = {},
        level = 0
    )
}