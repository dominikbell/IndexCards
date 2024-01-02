package com.example.indexcards.ui.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            modifier = modifier
                .height(300.dp),
            shape = AlertDialogDefaults.shape,
            color = AlertDialogDefaults.containerColor,
            tonalElevation = AlertDialogDefaults.TonalElevation,
        ) {
            Column(
                modifier = modifier
                    .padding(15.dp)
            ) {
                Row(
                    modifier = modifier.wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SelectionContainer(
                        modifier = modifier.weight(1f),
                    ) {
                        Text(
                            text = cardUiState.cardDetails.word,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    IconButton(
                        onClick = {
                            editCardViewModel.viewModelScope.launch {
                                editCardViewModel.updateUiState(cardUiState.cardDetails)
                                editCardViewModel.setCurrentCard(cardUiState.cardDetails.id)
                            }
                            showEditCardDialog()
                        }
                    ) {
                        Icon(
                            Icons.Default.Create,
                            modifier = Modifier.size(MaterialTheme.typography.titleLarge.fontSize.value.dp),
                            contentDescription = "Edit",
                        )
                    }
                }
                Column(
                    modifier = modifier,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    SelectionContainer {
                        Text(
                            modifier = modifier
                                .fillMaxWidth(),
                            text = cardUiState.cardDetails.meaning,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize
                        )
                    }
                    Spacer(modifier = modifier.size(8.dp))

                    TagList(
                        tagList = cardWithTags.value.tagList,
                        onClick = {},
                        onLongClick = {},
                        selectedTags = cardWithTags.value.tagList
                    )

                    Row {
                        Text(text = "Notes: ", fontStyle = FontStyle.Italic)
                        SelectionContainer {
                            Text(text = cardUiState.cardDetails.notes)
                        }
                    }
                }
            }
        }
    }
}