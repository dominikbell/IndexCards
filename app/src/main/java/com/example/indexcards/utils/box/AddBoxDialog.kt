package com.example.indexcards.utils.box

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun AddBoxDialog(
    modifier: Modifier = Modifier,
    boxViewModel: BoxViewModel,
    onDismiss: () -> Unit,
) {
    var baxName by remember { mutableStateOf("") }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = { onDismiss() },
        text = {
            Column(
                modifier = modifier
            ) {
                OutlinedTextField(
                    value = baxName,
                    onValueChange = {
                        baxName = it
                    },
                    label = { Text(text = "the word") },
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                boxViewModel.setName(baxName)
                onDismiss()
            }) {
                Text(text = "Save")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismiss()
            }) {
                Text(text = "Cancel")
            }
        }
    )
}