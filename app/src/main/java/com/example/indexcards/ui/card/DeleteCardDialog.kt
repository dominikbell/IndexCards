package com.example.indexcards.ui.card

import android.widget.Toast
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.utils.AppViewModelProvider
import com.example.indexcards.utils.card.EditCardViewModel
import kotlinx.coroutines.launch

@Composable
fun DeleteCardDialog(
    modifier: Modifier = Modifier,
    hideDialog: () -> Unit,
    editCardViewModel: EditCardViewModel = viewModel(
        factory = AppViewModelProvider(context = LocalContext.current).factory
    ),
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    AlertDialog(
        modifier = modifier,
        onDismissRequest = hideDialog,
        confirmButton =
        {
            TextButton(
                onClick = {
                    coroutineScope.launch {
                        Toast.makeText(context, "Deleting Card Nr. ${editCardViewModel.idOfCardToBeDeleted}", Toast.LENGTH_SHORT).show()
                        editCardViewModel.deleteCard()
                        editCardViewModel.resetIdOfCardToBeDeleted()
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
                onClick = hideDialog
            ) {
                Text(text = "Cancel")
            }
        },
        text = { Text(text = "Are you sure you want to delete this card?") },
        title = { Text(text = "Delete Card") }
    )
}