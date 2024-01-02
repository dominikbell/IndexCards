package com.example.indexcards.ui.card

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.utils.ViewModelProvider
import com.example.indexcards.utils.card.EditCardViewModel
import kotlinx.coroutines.launch

@Composable
fun DeleteCardDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    editCardViewModel: EditCardViewModel = viewModel(
        factory = ViewModelProvider(context = LocalContext.current).factory
    ),
) {
    val currentCard = editCardViewModel.currentCard.collectAsState().value

    AlertDialog(
        modifier = modifier,
        text = { Text(text = "Are you sure you want to delete this card?") },
        title = { Text(text = "Delete Card for '${currentCard.word}'") },
        onDismissRequest = onDismiss,
        confirmButton =
        {
            TextButton(
                onClick = {
                    editCardViewModel.viewModelScope.launch {
                        editCardViewModel.deleteCard(currentCard.cardId)
                    }
                    onDismiss()
                }
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
    )
}