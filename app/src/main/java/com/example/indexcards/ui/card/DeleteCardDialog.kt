package com.example.indexcards.ui.card

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.utils.AppViewModelProvider
import com.example.indexcards.utils.box.EditBoxViewModel
import kotlinx.coroutines.launch

@Composable
fun DeleteCardDialog(
    modifier: Modifier = Modifier,
    hideDialog: () -> Unit,
    editBoxViewModel: EditBoxViewModel = viewModel(
        factory = AppViewModelProvider(context = LocalContext.current).factory
    ),
) {
    val coroutineScope = rememberCoroutineScope()

    AlertDialog(
        modifier = modifier,
        text = { Text(text = "Are you sure you want to delete this card?") },
        title = { Text(text = "Delete Card") },
        onDismissRequest = hideDialog,
        confirmButton =
        {
            TextButton(
                onClick = {
                    coroutineScope.launch {
                        editBoxViewModel.deleteCard()
                        editBoxViewModel.resetIdOfCardToBeDeleted()
                    }
                    hideDialog()
                }
            ) {
                Text(text = "Delete")
            }
        },
        dismissButton =
        {
            TextButton(
                onClick = {
                    editBoxViewModel.resetIdOfCardToBeDeleted()
                    hideDialog()
                }
            ) {
                Text(text = "Cancel")
            }
        },
    )
}