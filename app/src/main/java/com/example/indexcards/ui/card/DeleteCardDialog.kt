package com.example.indexcards.ui.card

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.R
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
    val currentCard by editCardViewModel.currentCard.collectAsState()

    AlertDialog(
        modifier = modifier,
        text = { Text(text = stringResource(R.string.delete_card_sure)) },
        title = { Text(text = stringResource(R.string.delete_card) + " '${currentCard.word}'") },
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