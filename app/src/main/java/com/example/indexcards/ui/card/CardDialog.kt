package com.example.indexcards.ui.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.ui.tag.TagList
import com.example.indexcards.utils.ViewModelProvider
import com.example.indexcards.utils.card.CardViewModel
import com.example.indexcards.utils.card.EditCardViewModel
import kotlinx.coroutines.launch

@Composable
fun CardDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    showEditCardDialog: () -> Unit,
    cardViewModel: CardViewModel = viewModel(
        factory = ViewModelProvider(context = LocalContext.current).factory
    ),
    editCardViewModel: EditCardViewModel = viewModel(
        factory = ViewModelProvider(context = LocalContext.current).factory
    )
) {
    val cardUiState = cardViewModel.cardUiState
    val cardWithTags = cardViewModel.cardWithTags.collectAsState()

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = cardUiState.cardDetails.word,
                )
                IconButton(
                    onClick = {
                        editCardViewModel.viewModelScope.launch {
                            editCardViewModel.updateUiState(cardUiState.cardDetails)
                            editCardViewModel.setCurrentCard(cardUiState.cardDetails.id)
                        }
                        showEditCardDialog()
                    }
                ) {
                    Icon(Icons.Default.Create, contentDescription = "Edit")
                }
            }
        },
        text = {
            Column {
                Text(text = cardUiState.cardDetails.meaning)

                TagList(
                    tagList = cardWithTags.value.tagList,
                    onClick = {},
                    onLongClick = {},
                    selectedTags = cardWithTags.value.tagList
                )

                Text(text = cardUiState.cardDetails.notes)
            }
        },

        confirmButton = {}
    )
}