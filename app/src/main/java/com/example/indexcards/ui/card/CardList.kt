package com.example.indexcards.ui.card

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.data.Card
import com.example.indexcards.data.Tag
import com.example.indexcards.ui.tag.TagList
import com.example.indexcards.utils.ViewModelProvider
import com.example.indexcards.utils.card.CardViewModel
import com.example.indexcards.utils.card.EditCardViewModel
import com.example.indexcards.utils.card.toCardDetails
import kotlinx.coroutines.launch

@Composable
fun CardList(
    modifier: Modifier = Modifier,
    cardList: List<Card>,
    showDelete: () -> Unit,
    showDialog: () -> Unit,
    showEditDialog: () -> Unit,
    cardViewModel: CardViewModel = viewModel(
        factory = ViewModelProvider(context = LocalContext.current).factory
    ),
    editCardViewModel: EditCardViewModel = viewModel(
        factory = ViewModelProvider(context = LocalContext.current).factory
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
                    cardViewModel.viewModelScope.launch {
                        cardViewModel.setCurrentCard(it.cardId)
                        cardViewModel.updateUiState(it.toCardDetails())
                    }
                    showDialog()
                },
                onLongClick = {
                    editCardViewModel.viewModelScope.launch {
                        editCardViewModel.setCurrentCard(it.cardId)
                        editCardViewModel.updateUiState(it.toCardDetails())
                    }
                    showEditDialog()
                },
                showDelete = {
                    editCardViewModel.viewModelScope.launch {
                        editCardViewModel.setCurrentCard(it.cardId)
                    }
                    showDelete()
                },
                tagList = listOf()
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardListItem(
    modifier: Modifier = Modifier,
    item: Card,
    onClick: (Card) -> Unit,
    onLongClick: (Card) -> Unit,
    showDelete: (Card) -> Unit,
    tagList: List<Tag>
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .combinedClickable(
                onClick = { onClick(item) },
                onLongClick = { onLongClick(item) }
            ),
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
                TagList(
                    tagList = tagList,
                    onClick = {},
                    onLongClick = { /*TODO*/ },
                    selectedTags = listOf()
                )
            }

            IconButton(
                onClick = { showDelete(item) }
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete"
                )
            }
        }
    }
}