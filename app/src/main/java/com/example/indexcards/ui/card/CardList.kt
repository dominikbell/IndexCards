package com.example.indexcards.ui.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.data.Card
import com.example.indexcards.utils.AppViewModelProvider
import com.example.indexcards.utils.box.EditBoxViewModel
import com.example.indexcards.utils.card.EditCardViewModel
import com.example.indexcards.utils.card.toCardDetails

@Composable
fun CardList(
    modifier: Modifier = Modifier,
    cardList: List<Card>,
    showDelete: () -> Unit,
    showEdit: () -> Unit,
    editCardViewModel: EditCardViewModel = viewModel(
        factory = AppViewModelProvider(context = LocalContext.current).factory
    ),
    editBoxViewModel: EditBoxViewModel = viewModel(
        factory = AppViewModelProvider(context = LocalContext.current).factory
    ),
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top
    ) {
        items(
            items = cardList,
            key = { it.cardId }
        ) { item ->
            CardListItem(
                item = item,
                onClick = {
                    editCardViewModel.updateUiState(it.toCardDetails())
                    showEdit()
                },
                showDelete = {
                    editBoxViewModel.idOfCardToBeDeleted = it
                    showDelete()
                }
            )
        }
    }
}

@Composable
fun CardListItem(
    modifier: Modifier = Modifier,
    item: Card,
    onClick: (Card) -> Unit,
    showDelete: (Long) -> Unit,
) {
    Card(
        modifier = modifier
            .clickable { onClick(item) }
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = modifier
                    .weight(1f)
                    .padding(10.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = item.word,
                    textAlign = TextAlign.Start,
                )
                Spacer(modifier = modifier.size(4.dp))
                Text(
                    text = item.meaning,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            IconButton(
                onClick = {
                    showDelete(item.cardId)
                }
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete"
                )
            }
        }
    }
}