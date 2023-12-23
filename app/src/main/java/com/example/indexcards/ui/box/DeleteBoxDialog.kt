package com.example.indexcards.ui.box

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.indexcards.data.Box

@Composable
fun DeleteBoxDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    deleteBox: () -> Unit,
    boxToBeDeleted: Box,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton =
        {
            TextButton(
                onClick = deleteBox
            ) {
                Text(text = "Delete")
            }
        },
        dismissButton =
        {
            TextButton(
                onClick = onDismiss
            ) {
                Text(text = "Cancel")
            }
        },
        text = { Text(text = "Are you sure you want to delete the box ${boxToBeDeleted.name}?") },
        title = { Text(text = "Delete this Box") }
    )
}