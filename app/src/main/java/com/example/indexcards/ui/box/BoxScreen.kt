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
import com.example.indexcards.utils.card.toCardState
import com.example.indexcards.utils.tag.emptyTag
import com.example.indexcards.utils.tag.toColor
import com.example.indexcards.utils.tag.toTagDetails
import kotlinx.coroutines.launch

@Composable
fun BoxScreen(
    modifier: Modifier = Modifier,
    boxId: Long, /* Is only here for boxScreenViewModel to work */
    startLevel: Int = -1,
    hasNotificationPermission: Boolean = false,
    navigateToBoxesOverview: () -> Unit = {},
    requestNotificationPermission: () -> Unit = {},
    scheduleNotification: (Long) -> Unit = {},
    boxScreenViewModel: BoxScreenViewModel = viewModel(
        factory = ViewModelProvider(context = LocalContext.current).factory
    ),
) {
    /** Navigation on the BoxScreen */
    val boxScreenState = boxScreenViewModel.boxScreenState

    /** uiStates of Box (fixed) and Card (dynamic) */
    val boxUiState = boxScreenViewModel.boxUiState
    val cardUiState = boxScreenViewModel.cardUiState
    val tagUiState = boxScreenViewModel.tagUiState
    val tagSelected by boxScreenViewModel.tagSelected.collectAsState()
    val levelSelected by boxScreenViewModel.levelSelected.collectAsState()
    val trainingCounts by boxScreenViewModel.trainingCounts.collectAsState()
    val boxWithTags by boxScreenViewModel.uiBoxWithTags.collectAsState()
    val cardsWithTags by boxScreenViewModel.uiCardsWithTags.collectAsState()
    val cardWithTags by boxScreenViewModel.uiCardWithTags.collectAsState()
    val tagWithCards by boxScreenViewModel.uiTagWithCards.collectAsState()
    val currentCard by boxScreenViewModel.currentCard.collectAsState()

    var cardDialog by remember { mutableStateOf(false) }
    var noCardsDialog by remember { mutableStateOf(false) }
    var newCardDialog by remember { mutableStateOf(false) }
    var editCardDialog by remember { mutableStateOf(false) }
    var deleteCardDialog by remember { mutableStateOf(false) }
    var newTag by remember { mutableStateOf(true) }
    var tagDialog by remember { mutableStateOf(false) }
    var deleteBoxDialog by remember { mutableStateOf(false) }

    val filteredCardWithTagList =
        if (levelSelected == -1) {
            if (tagSelected == emptyTag) {
                cardsWithTags.cardWithTagList
            } else {
                cardsWithTags.cardWithTagList.filter { it.tags.contains(tagSelected) }
            }
        } else {
            if (tagSelected == emptyTag) {
                cardsWithTags.cardWithTagList.filter { it.card.level == levelSelected }
            } else {
                cardsWithTags.cardWithTagList.filter {
                    it.card.level == levelSelected && it.tags.contains(tagSelected)
                }
            }
        }

    val shuffledCardList = filteredCardWithTagList.shuffled()

    LaunchedEffect(key1 = startLevel) {
        if (startLevel != -1) {
            boxScreenViewModel.setLevelSelected(startLevel)
            if (boxScreenViewModel.getNumberOfCardsOfLevelInBox(level = startLevel) != 0) {
                /* TODO: A bit hacky using this suspend function but without it the state is not
                    updated yet and cardsWithTags is still empty */
                boxScreenViewModel.changeTrainingCounts(true)
                boxScreenViewModel.updateBoxScreenState(BoxScreenState.TRAIN)
            } else {
                noCardsDialog = true
            }
        }
    }

    fun showEditTagDialog(tag: Tag) {
        boxScreenViewModel.setColor(tag.color)
        boxScreenViewModel.setTagUiState(tag.toTagDetails())
        newTag = false; tagDialog = true
    }

    fun showNewTagDialog() {
        newTag = true; tagDialog = true
    }

    BackHandler {
        when (boxScreenState) {
            BoxScreenState.VIEW -> {
                navigateToBoxesOverview()
            }

            BoxScreenState.EDIT -> {
                boxScreenViewModel.updateBoxScreenState(BoxScreenState.VIEW)
            }

            BoxScreenState.TRAIN -> {
                boxScreenViewModel.updateBoxScreenState(BoxScreenState.VIEW)
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            BoxScreenTopBar(
                navigateToBoxesOverview = navigateToBoxesOverview,
                updateEditUiStatus = {
                    boxScreenViewModel.updateBoxUiState(boxWithTags.box.toBoxDetails())
                },
                changeBoxScreenState = { boxScreenViewModel.updateBoxScreenState(it) },
                boxScreenState = boxScreenState,
                thisBox = boxWithTags.box,
                cancelEdit = { boxScreenViewModel.updateBoxScreenState(BoxScreenState.VIEW) },
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
                        onClick = { deleteBoxDialog = true }
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
                        boxScreenViewModel.setCurrentCard(it)
                        cardDialog = true
                    },
                    showEditCardDialog = {
                        boxScreenViewModel.setCardUiStateFromCurrentCard()
                        editCardDialog = true
                    },
                    showNewTagDialog = { showNewTagDialog() },
                    onTagLongClick = { showEditTagDialog(it) },
                    levelSelected = levelSelected,
                    selectLevel = { boxScreenViewModel.setLevelSelected(it) },
                    setTagSortedBy = { boxScreenViewModel.setTagSelected(it) },
                    resetTagSortedBy = { boxScreenViewModel.resetTagSelected() },
                    boxWithTags = boxWithTags,
                    cardsWithTags = cardsWithTags,
                    tagWithCards = tagWithCards,
                    filteredCardWithTagList = filteredCardWithTagList,
                )
            }

            BoxScreenState.EDIT -> {
                BoxScreenEditing(
                    modifier = modifier.padding(innerPadding),
                    boxUiState = boxUiState,
                    onSave = {
                        boxScreenViewModel.saveBox()
                        boxScreenViewModel.updateBoxScreenState(BoxScreenState.VIEW)
                    },
                    hasNotificationPermission = hasNotificationPermission,
                    requestNotificationPermission = { requestNotificationPermission() },
                    updateBoxUiState = { boxScreenViewModel.updateBoxUiState(it) },
                )
            }

            BoxScreenState.TRAIN -> {
                TrainingScreen(
                    modifier = modifier.padding(innerPadding),
                    navigateToBoxScreen = {
                        /* TODO: Set reminder for next training */
                        boxScreenViewModel.setLevelSelected(-1)
                        boxScreenViewModel.resetTagSelected()
                        boxScreenViewModel.updateBoxScreenState(BoxScreenState.VIEW)
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
            onDismiss = {
                cardDialog = false
                boxScreenViewModel.resetCardUiState()
            },
            cardWithTags = cardWithTags,
            showEditCardDialog = {
                boxScreenViewModel.setCardUiStateFromCurrentCard()
                editCardDialog = true
            },
            isEditing = editCardDialog,
            showDelete = { deleteCardDialog = true },
        )
    }

    if (newCardDialog) {
        NewCardDialog(
            cardUiState = cardUiState,
            boxWithTags = boxWithTags,
            updateUiState = { boxScreenViewModel.updateCardState(cardDetails = it) },
            onDismiss = {
                newCardDialog = false
                boxScreenViewModel.resetCard()
            },
            saveCard = {
                newCardDialog = false
                boxScreenViewModel.saveCard(doReset = true)
            },
            onTagClick = {
                if (cardUiState.tagList.contains(it)) {
                    boxScreenViewModel.updateCardState(tagList = cardUiState.tagList.minus(it))
                } else {
                    boxScreenViewModel.updateCardState(tagList = cardUiState.tagList.plus(it))
                }
            },
            showNewTagDialog = { showNewTagDialog() },
            showEditTagDialog = { showEditTagDialog(it) },
        )
    }

    if (editCardDialog) {
        EditCardDialog(
            boxWithTags = boxWithTags,
            cardWithTags = cardWithTags,
            cardUiState = cardUiState,
            updateUiState = { boxScreenViewModel.updateCardState(cardDetails = it) },
            onDismiss = {
                boxScreenViewModel.updateCardState(cardWithTags.toCardState(true))
                editCardDialog = false
            },
            onDeleteCard = { deleteCardDialog = true },
            showNewTagDialog = { showNewTagDialog() },
            showEditTagDialog = { showEditTagDialog(it) },
            saveCard = {
                editCardDialog = false
                boxScreenViewModel.saveCard(doReset = false)
            },
            clickOnTag = {
                if (cardUiState.tagList.contains(it)) {
                    boxScreenViewModel.updateCardState(tagList = cardUiState.tagList.minus(it))
                } else {
                    boxScreenViewModel.updateCardState(tagList = cardUiState.tagList.plus(it))
                }
            }
        )
    }

    if (deleteCardDialog) {
        DeleteCardDialog(
            currentCard = currentCard,
            onDismiss = {
                deleteCardDialog = false
            },
            deleteCard = {
                cardDialog = false
                editCardDialog = false
                deleteCardDialog = false
                boxScreenViewModel.deleteCard(currentCard)
            }
        )
    }

    if (tagDialog) {
        TagDialog(
            modifier = Modifier,
            newTag = newTag,
            tagUiState = tagUiState,
            initialColor = boxScreenViewModel.colorUiState.toColor(),
            onDismiss = {
                tagDialog = false
                boxScreenViewModel.resetTagUiState()
            },
            updateUiState = { boxScreenViewModel.setTagUiState(it) },
            saveTag = {
                tagDialog = false
                boxScreenViewModel.saveNewTag(
                    addToCard = (editCardDialog || newCardDialog)
                )
            },
            updateTag = {
                tagDialog = false
                boxScreenViewModel.updateTag()
            },
            setColor = { boxScreenViewModel.setColor(it) },
            onDelete = {
                tagDialog = false
                boxScreenViewModel.deleteTag()
                boxScreenViewModel.resetTagUiState()
            }
        )
    }

    if (deleteBoxDialog) {
        DeleteBoxDialog(
            onDismiss = { deleteBoxDialog = false },
            onDelete = {
                navigateToBoxesOverview()
                boxScreenViewModel.deleteBox(boxId = boxId)
            },
            boxToBeDeleted = boxWithTags.box
        )
    }

    if (noCardsDialog) {
        NoCardsDialog(
            onDismiss = {
                boxScreenViewModel.setLevelSelected(-1)
                noCardsDialog = false
            },
            level = levelSelected
        )
    }
}