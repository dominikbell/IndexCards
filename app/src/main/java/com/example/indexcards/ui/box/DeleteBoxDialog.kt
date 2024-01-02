package com.example.indexcards.ui.box

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.data.Box
import com.example.indexcards.utils.ViewModelProvider
import com.example.indexcards.utils.box.BoxViewModel

@Composable
fun DeleteBoxDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    boxToBeDeleted: Box,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        text = { Text(text = "Are you sure you want to delete this box?") },
        title = { Text(text = "Delete box '${boxToBeDeleted.name}'") },
        dismissButton =
        {
            TextButton(
                onClick = onDismiss
            ) {
                Text(text = "Cancel")
            }
        },
        confirmButton =
        {
            TextButton(
                onClick = onDelete
            ) {
                Text(text = "Delete")
            }
        },
    )
}