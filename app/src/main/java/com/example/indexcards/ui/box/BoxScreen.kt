package com.example.indexcards.ui.box

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.data.Box
import com.example.indexcards.data.Tag
import com.example.indexcards.ui.card.CardDialog
import com.example.indexcards.ui.card.CardList
import com.example.indexcards.ui.card.DeleteCardDialog
import com.example.indexcards.ui.home.NewTagButton
import com.example.indexcards.ui.tag.TagDialog
import com.example.indexcards.utils.AppViewModelProvider
import com.example.indexcards.utils.box.EditBoxViewModel
import com.example.indexcards.utils.box.toBox
import com.example.indexcards.utils.card.EditCardViewModel

@Composable
fun BoxScreen(
    modifier: Modifier = Modifier,
    navigateToBoxesOverview: () -> Unit,
    navigateToEditBoxScreen: (Long) -> Unit,
    boxId: Long,
    editBoxViewModel: EditBoxViewModel = viewModel(
        factory = AppViewModelProvider(context = LocalContext.current).factory
    ),
) {
    val boxUiState = editBoxViewModel.boxUiState
    var newCard by remember { mutableStateOf(true) }
    var cardDialog by remember { mutableStateOf(false) }
    var deleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            BoxTopBar(
                navigateToBoxesOverview = navigateToBoxesOverview,
                navigateToEditBoxScreen = { navigateToEditBoxScreen(boxId) },
                thisBox = boxUiState.boxDetails.toBox()
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    newCard = true
                    cardDialog = true
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        BoxScreenBody(
            modifier = modifier
                .padding(innerPadding),
            thisBox = boxUiState.boxDetails.toBox(),
            showDelete = { deleteDialog = true },
            showEdit = {
                newCard = false
                cardDialog = true
            }
        )
    }

    if (cardDialog) {
        CardDialog(
            hideDialog = {
                cardDialog = false
            },
            newCard = newCard
        )
    }

    if (deleteDialog) {
        DeleteCardDialog(
            hideDialog = { deleteDialog = false }
        )
    }
}

@Composable
fun BoxScreenBody(
    modifier: Modifier = Modifier,
    thisBox: Box,
    showDelete: () -> Unit,
    showEdit: () -> Unit,
    editBoxViewModel: EditBoxViewModel = viewModel(
        factory = AppViewModelProvider(context = LocalContext.current).factory
    ),
) {
    val numberOfCards = editBoxViewModel.numberOfCards.collectAsState()
    val boxWithTags = editBoxViewModel.boxWithTags.collectAsState()
    val boxWithCards = editBoxViewModel.boxWithCards.collectAsState()
    val context = LocalContext.current

    var listVisible by remember { mutableStateOf(false) }
    var tagDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            text = "Description: ${thisBox.description}",
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleLarge,
        )

        Text(text = "Number of Cards in this box: ${numberOfCards.value}")

        Spacer(modifier = Modifier.size(4.dp))

        val rowModifier: Modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp)
        Row(
            modifier = rowModifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LazyRow(
                modifier = rowModifier
                    .weight(1f)
            ) {
                items(
                    items = boxWithTags.value.tagList,
                    key = { it.text }
                ) { item ->
                    TagListItem(
                        modifier = rowModifier,
                        item = item
                    ) {
                        Toast.makeText(context, "Tag Nr ${item.tagId}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            NewTagButton(onClick = { tagDialog = true })
        }

        Spacer(modifier = Modifier.size(4.dp))

        if (boxWithCards.value.cardList.isEmpty()) {
            Text(
                text = "Click '+' to add a new card to this box",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
            )
        } else {
            Button(onClick = { listVisible = !listVisible }) {
                if (listVisible) {
                    Text(text = "Hide card list")
                } else {
                    Text(text = "Show card list")
                }
            }

            Spacer(modifier = Modifier.size(6.dp))

            if (listVisible) {
                CardList(
                    cardList = boxWithCards.value.cardList,
                    showEdit = showEdit,
                    showDelete = showDelete
                )
            }
        }
    }

    if (tagDialog) {
        TagDialog(
            modifier = Modifier,
            hideDialog = { tagDialog = false }
        )
    }
}

@Composable
fun TagListItem(
    modifier: Modifier = Modifier,
    item: Tag,
    onClick: () -> Unit
) {
    Text(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(
                color = Color(android.graphics.Color.parseColor("#${item.color}"))
            )
            .clickable {
                onClick()
            }
            .padding(4.dp),
        text = item.text
    )
}