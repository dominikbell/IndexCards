package com.example.indexcards.ui.box

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.data.Tag
import com.example.indexcards.ui.dialogs.CardDialog
import com.example.indexcards.ui.dialogs.DeleteCardDialog
import com.example.indexcards.ui.dialogs.EditCardDialog
import com.example.indexcards.ui.dialogs.NewCardDialog
import com.example.indexcards.ui.dialogs.DeleteBoxDialog
import com.example.indexcards.ui.dialogs.NoCardsDialog
import com.example.indexcards.ui.tag.TagDialog
import com.example.indexcards.utils.ViewModelProvider
import com.example.indexcards.utils.box.BoxScreenState
import com.example.indexcards.utils.box.BoxScreenViewModel
import com.example.indexcards.utils.box.toBoxDetails
import com.example.indexcards.utils.card.CardViewModel
import com.example.indexcards.utils.card.EditCardViewModel
import com.example.indexcards.utils.card.toCardDetails
import com.example.indexcards.utils.tag.EditTagViewModel
import com.example.indexcards.utils.tag.emptyTag
import com.example.indexcards.utils.tag.toTagDetails
import kotlinx.coroutines.launch

@Composable
fun BoxScreen(
    modifier: Modifier = Modifier,
    navigateToBoxesOverview: () -> Unit,
    boxId: Long, /* Is only here for boxScreenViewModel to work */
    startLevel: Int = -1,
    boxScreenViewModel: BoxScreenViewModel,
    cardViewModel: CardViewModel = viewModel(
        factory = ViewModelProvider(context = LocalContext.current).factory
    ),
    editCardViewModel: EditCardViewModel = viewModel(
        factory = ViewModelProvider(context = LocalContext.current).factory
    ),
    editTagViewModel: EditTagViewModel = viewModel(
        factory = ViewModelProvider(context = LocalContext.current).factory
    ),
    hasNotificationPermission: Boolean = false,
    requestNotificationPermission: () -> Unit = {},
    scheduleNotification: (Long) -> Unit = {}
) {
    val boxScreenState = boxScreenViewModel.boxScreenState
    val cardUiState = cardViewModel.cardUiState
    val tagSortedBy by boxScreenViewModel.tagSortedBy.collectAsState()
    val levelSelected by boxScreenViewModel.levelSelected.collectAsState()
    val trainingCounts by boxScreenViewModel.trainingCounts.collectAsState()
    val boxWithTags by boxScreenViewModel.boxWithTags.collectAsState()
    val cardsWithTags by boxScreenViewModel.cardsWithTags.collectAsState()
    val cardWithTags by cardViewModel.cardWithTags.collectAsState()
    val tagWithCards by boxScreenViewModel.tagWithCards.collectAsState()
    val currentCard by editCardViewModel.currentCard.collectAsState()

    var cardDialog by remember { mutableStateOf(false) }
    var noCardsDialog by remember { mutableStateOf(false) }
    var newCardDialog by remember { mutableStateOf(false) }
    var editCardDialog by remember { mutableStateOf(false) }
    var deleteCardDialog by remember { mutableStateOf(false) }
    var newTag by remember { mutableStateOf(true) }
    var tagDialog by remember { mutableStateOf(false) }
    var deleteDialog by remember { mutableStateOf(false) }

    val filteredCardWithTagList =
        if (levelSelected == -1) {
            if (tagSortedBy == emptyTag) {
                cardsWithTags.cardWithTagList
            } else {
                cardsWithTags.cardWithTagList.filter { it.tags.contains(tagSortedBy) }
            }
        } else {
            if (tagSortedBy == emptyTag) {
                cardsWithTags.cardWithTagList.filter { it.card.level == levelSelected }
            } else {
                cardsWithTags.cardWithTagList.filter {
                    it.card.level == levelSelected && it.tags.contains(tagSortedBy)
                }
            }
        }

    val shuffledCardList = filteredCardWithTagList.shuffled()

    LaunchedEffect(key1 = startLevel) {
        if (startLevel != -1) {
            boxScreenViewModel.updateSelectedLevel(startLevel)
            if (boxScreenViewModel.getNumberOfCardsOfLevelInBox(level = startLevel) != 0) {
                /* TODO: A bit hacky using this suspend function but without it the state is not
                    updated yet and cardsWithTags is still empty */
                boxScreenViewModel.changeTrainingCounts(true)
                boxScreenViewModel.changeBoxScreenState(BoxScreenState.TRAIN)
            } else {
                noCardsDialog = true
            }
        }
    }

    fun showEditTagDialog() {
        newTag = false; tagDialog = true
    }

    fun showNewTagDialog() {
        newTag = true; tagDialog = true
    }

    fun onTagLongClick(item: Tag) {
        editTagViewModel.setColor(item.color)
        editTagViewModel.updateUiState(item.toTagDetails())
    }

    BackHandler {
        when (boxScreenState) {
            BoxScreenState.VIEW -> {
                navigateToBoxesOverview()
            }

            BoxScreenState.EDIT -> {
                boxScreenViewModel.changeBoxScreenState(BoxScreenState.VIEW)
            }

            BoxScreenState.TRAIN -> {
                boxScreenViewModel.changeBoxScreenState(BoxScreenState.VIEW)
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            BoxScreenTopBar(
                navigateToBoxesOverview = navigateToBoxesOverview,
                updateEditUiStatus = {
                    boxScreenViewModel.updateUiState(boxWithTags.box.toBoxDetails())
                },
                changeBoxScreenState = { boxScreenViewModel.changeBoxScreenState(it) },
                boxScreenState = boxScreenState,
                thisBox = boxWithTags.box,
                cancelEdit = { boxScreenViewModel.changeBoxScreenState(BoxScreenState.VIEW) },
                trainingCounts = trainingCounts,
                changeTrainingCounts = {
                    boxScreenViewModel.changeTrainingCounts()
                }
            )
        },

        floatingActionButton = {
            when (boxScreenState) {
                BoxScreenState.VIEW -> {
                    FloatingActionButton(
                        onClick = { newCardDialog = true }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                }

                BoxScreenState.EDIT -> {
                    FloatingActionButton(
                        onClick = { deleteDialog = true }
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }

                BoxScreenState.TRAIN -> {}
            }
        }
    ) { innerPadding ->
        when (boxScreenState) {
            BoxScreenState.VIEW -> {
                BoxScreenBody(
                    modifier = modifier.padding(innerPadding),
                    showCardDialog = {
                        cardViewModel.viewModelScope.launch {
                            cardViewModel.setCurrentCard(it.cardId)
                            cardViewModel.updateUiState(it.toCardDetails())
                        }
                        cardDialog = true
                    },
                    showEditCardDialog = {
                        editCardViewModel.viewModelScope.launch {
                            editCardViewModel.setCurrentCard(it.cardId)
                            editCardViewModel.updateUiState(it.toCardDetails())
                        }
                        editCardDialog = true
                    },
                    showNewTagDialog = { showNewTagDialog() },
                    onTagLongClick = { onTagLongClick(it) },
                    levelSelected = levelSelected,
                    selectLevel = { boxScreenViewModel.updateSelectedLevel(it) },
                    setTagSortedBy = { boxScreenViewModel.setTagSortedBy(it) },
                    resetTagSortedBy = { boxScreenViewModel.resetTagSortedBy() },
                    boxWithTags = boxWithTags,
                    cardsWithTags = cardsWithTags,
                    tagWithCards = tagWithCards,
                    filteredCardWithTagList = filteredCardWithTagList,
                )
            }

            BoxScreenState.EDIT -> {
                BoxScreenEditing(
                    modifier = modifier.padding(innerPadding),
                    onSave = {
                        boxScreenViewModel.saveBox()
                        boxScreenViewModel.changeBoxScreenState(BoxScreenState.VIEW)
                    },
                    boxScreenViewModel = boxScreenViewModel,
                    hasNotificationPermission = hasNotificationPermission,
                    requestNotificationPermission = { requestNotificationPermission() }
                )
            }

            BoxScreenState.TRAIN -> {
                TrainingScreen(
                    modifier = modifier.padding(innerPadding),
                    navigateToBoxScreen = {
                        /* TODO: Set reminder for next training */
                        boxScreenViewModel.updateSelectedLevel(-1)
                        boxScreenViewModel.resetTagSortedBy()
                        boxScreenViewModel.changeBoxScreenState(BoxScreenState.VIEW)
                    },
                    cardList = shuffledCardList,
                    onCardCorrect = {
                        boxScreenViewModel.viewModelScope.launch {
                            boxScreenViewModel.onCardCorrect(it)
                        }
                    },
                    onCardIncorrect = {
                        boxScreenViewModel.viewModelScope.launch {
                            boxScreenViewModel.onCardIncorrect(it)
                        }
                    },
                    trainingCounts = trainingCounts
                )
            }
        }
    }

    if (cardDialog) {
        CardDialog(
            onDismiss = { cardDialog = false },
            cardWithTags = cardWithTags,
            showEditCardDialog = {
                editCardViewModel.viewModelScope.launch {
                    editCardViewModel.updateUiState(cardUiState.cardDetails)
                    editCardViewModel.setCurrentCard(cardUiState.cardDetails.id)
                }
                editCardDialog = true
            },
            isEditing = editCardDialog,
            showDelete = {
                editCardViewModel.viewModelScope.launch {
                    editCardViewModel.setCurrentCard(it.cardId)
                }
                deleteCardDialog = true
            },
        )
    }

    if (editCardDialog) {
        EditCardDialog(
            boxWithTags = boxWithTags,
            cardWithTags = cardWithTags,
            currentCard = currentCard,
            onDismiss = { editCardDialog = false },
            showCardDialog = { editCardDialog = false },
            onDeleteCard = {
                deleteCardDialog = true
            },
            showNewTagDialog = { showNewTagDialog() },
            showEditTagDialog = { showEditTagDialog() },
        )
    }

    if (newCardDialog) {
        NewCardDialog(
            onDismiss = { newCardDialog = false },
            boxWithTags = boxWithTags,
            showNewTagDialog = { showNewTagDialog() },
            showEditTagDialog = { showEditTagDialog() },
        )
    }

    if (deleteCardDialog) {
        DeleteCardDialog(
            currentCard = currentCard,
            onDismiss = { deleteCardDialog = false },
            deleteCard = {
                editCardViewModel.viewModelScope.launch {
                    editCardViewModel.deleteCard(currentCard.cardId)
                }
            }
        )
    }

    if (tagDialog) {
        TagDialog(
            modifier = Modifier,
            onDismiss = { tagDialog = false },
            newTag = newTag
        )
    }

    if (deleteDialog) {
        DeleteBoxDialog(
            onDismiss = { deleteDialog = false },
            onDelete = {
                navigateToBoxesOverview()
                boxScreenViewModel.deleteBox()
            },
            boxToBeDeleted = boxWithTags.box
        )
    }

    if (noCardsDialog) {
        NoCardsDialog(
            onDismiss = {
                boxScreenViewModel.updateSelectedLevel(-1)
                noCardsDialog = false
            },
            level = levelSelected
        )
    }
}