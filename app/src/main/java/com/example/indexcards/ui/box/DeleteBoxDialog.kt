package com.example.indexcards.ui.box

import android.widget.Toast
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.data.Box
import com.example.indexcards.utils.box.HomeScreenViewModel
import com.example.indexcards.utils.AppViewModelProvider

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