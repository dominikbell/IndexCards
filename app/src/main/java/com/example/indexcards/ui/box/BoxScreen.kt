package com.example.indexcards.ui.box

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.ui.card.CardDialog
import com.example.indexcards.ui.card.CardList
import com.example.indexcards.ui.card.DeleteCardDialog
import com.example.indexcards.ui.card.EditCardDialog
import com.example.indexcards.ui.home.NewTagButton
import com.example.indexcards.ui.tag.TagList
import com.example.indexcards.utils.ViewModelProvider
import com.example.indexcards.utils.box.BoxScreenViewModel
import com.example.indexcards.utils.tag.emptyTag
import kotlinx.coroutines.launch

@Composable
fun BoxScreen(
    modifier: Modifier = Modifier,
    navigateToBoxesOverview: () -> Unit,
    navigateToEditBoxScreen: (Long) -> Unit,
    boxId: Long,
    boxScreenViewModel: BoxScreenViewModel = viewModel(
        factory = ViewModelProvider(context = LocalContext.current).factory
    ),
) {
    val boxWithCards = boxScreenViewModel.boxWithCards.collectAsState()
    val boxWithTags = boxScreenViewModel.boxWithTags.collectAsState()

    var newCard by remember { mutableStateOf(true) }
    var cardDialog by remember { mutableStateOf(false) }
    var editCardDialog by remember { mutableStateOf(false) }
    var deleteCardDialog by remember { mutableStateOf(false) }
    var newTag by remember { mutableStateOf(true) }
    var tagDialog by remember { mutableStateOf(false) }

    fun showDeleteCardDialog() {
        deleteCardDialog = true
    }

    fun showCardDialog() {
        cardDialog = true
    }

    fun showEditCardDialog() {
        newCard = false; editCardDialog = true
    }

    fun showNewCardDialog() {
        newCard = true; editCardDialog = true
    }

    fun hideCardDialogs() {
        cardDialog = false; editCardDialog = false
    }

    fun showEditTagDialog() {
        newTag = false; tagDialog = true
    }

    fun showNewTagDialog() {
        newTag = true; tagDialog = true
    }

    fun onTagDialogDismiss() {
        tagDialog = false
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            BoxTopBar(
                navigateToBoxesOverview = navigateToBoxesOverview,
                navigateToEditBoxScreen = {
                    navigateToEditBoxScreen(boxId)
                },
                thisBox = boxWithCards.value.box
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    newCard = true
                    editCardDialog = true
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        BoxScreenBody(
            modifier = modifier
                .padding(innerPadding),
            showCard = { showCardDialog() },
            showEditCardDialog = { showEditCardDialog() },
            showCardDelete = { showDeleteCardDialog() }
        )
    }

    if (cardDialog) {
        CardDialog(
            onDismiss = { hideCardDialogs() },
            showEditCardDialog = { showEditCardDialog() }
        )
    }

    if (editCardDialog) {
        EditCardDialog(
            onDismiss = { hideCardDialogs() },
            newCard = newCard,
            boxWithTags = boxWithTags.value,
            showDeleteCard = {
                hideCardDialogs()
                showDeleteCardDialog()
            },
            showNewTagDialog = { showNewTagDialog() },
            showEditTagDialog = { showEditTagDialog() },
        )
    }

    if (deleteCardDialog) {
        DeleteCardDialog(
            onDismiss = { deleteCardDialog = false },
        )
    }

//    if (tagDialog) {
//        TagDialog(
//            modifier = Modifier,
//            hideDialog = { tagDialog = false },
//            newTag = true
//        )
//    }
}

@Composable
fun BoxScreenBody(
    modifier: Modifier = Modifier,
    showCard: () -> Unit,
    showCardDelete: () -> Unit,
    showEditCardDialog: () -> Unit,
//    showEditTagDialog: () -> Unit,
//    showNewTagDialog: () -> Unit,
//    onCardDismiss: () -> Unit,
//    onTagDismiss: () -> Unit,
    boxScreenViewModel: BoxScreenViewModel = viewModel(
        factory = ViewModelProvider(context = LocalContext.current).factory
    ),
) {
    val boxWithTags = boxScreenViewModel.boxWithTags.collectAsState()
    val boxWithCards = boxScreenViewModel.boxWithCards.collectAsState()
    val tagWithCards = boxScreenViewModel.tagWithCards.collectAsState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            text = "Description: ${boxWithCards.value.box.description}",
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleLarge,
        )

        Text(text = "Number of Cards in this box: ${boxWithCards.value.cardList.size}")

        Spacer(modifier = Modifier.size(4.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
        ) {
            TagList(
                modifier = Modifier.weight(1f),
                tagList = boxWithTags.value.tagList,
                onClick = {
                    if (tagWithCards.value.tag == emptyTag) {
                        boxScreenViewModel.viewModelScope.launch {
                            boxScreenViewModel.getTagWithCards(it)
                        }
                    } else {
                        boxScreenViewModel.resetTagWithCards()
                    }
                },
                onLongClick = {}
//                        showEditTagDialog
            )

            NewTagButton(onClick = {}
//            showNewTagDialog
            )
        }

        Spacer(modifier = Modifier.size(4.dp))

        if (boxWithCards.value.cardList.isEmpty()) {
            Text(
                text = "Click '+' to add a new card to this box",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
            )
        } else {
            if (tagWithCards.value.cardList.isEmpty()) {
                CardList(
                    cardList = boxWithCards.value.cardList,
                    showDialog = showCard,
                    showDelete = { showCardDelete() },
                    showEditDialog = { showEditCardDialog() }
                )
            } else {
//                CardList(
//                    cardList = tagWithCards.value.cardList,
//                    showEdit = showCard,
//                    showDelete = {}
////                    showCardDelete
//                )
            }
        }
    }

}