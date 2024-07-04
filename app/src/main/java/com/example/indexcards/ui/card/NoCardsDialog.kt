package com.example.indexcards.ui.card

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun NoCardsDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    level: Int
) {
    AlertDialog(
        modifier = modifier,
        title = { Text(text = "Oops, no cards found") },
        text = { Text(text = "There are no card in this box of level ${level+1}. This must be a mistake") },
        onDismissRequest = onDismiss,
        confirmButton = { },
        dismissButton =
        {
            TextButton(
                onClick = onDismiss
            ) {
                Text(text = "Go back to box")
            }
        },
    )

}