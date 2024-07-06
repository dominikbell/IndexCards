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
import com.example.indexcards.ui.card.CardDialog
import com.example.indexcards.ui.card.DeleteCardDialog
import com.example.indexcards.ui.card.EditCardDialog
import com.example.indexcards.ui.card.NewCardDialog
import com.example.indexcards.ui.elements.NoCardsDialog
import com.example.indexcards.ui.tag.TagDialog
import com.example.indexcards.utils.ViewModelProvider
import com.example.indexcards.utils.box.BoxScreenState
import com.example.indexcards.utils.box.BoxScreenViewModel
import com.example.indexcards.utils.box.toBoxDetails
import com.example.indexcards.utils.card.EditCardViewModel
import com.example.indexcards.utils.tag.emptyTag
import kotlinx.coroutines.launch

@Composable
fun BoxScreen(
    modifier: Modifier = Modifier,
    navigateToBoxesOverview: () -> Unit,
    boxId: Long, /* Is only here for boxScreenViewModel to work */
    startLevel: Int = -1,
    boxScreenViewModel: BoxScreenViewModel,
    editCardViewModel: EditCardViewModel = viewModel(
        factory = ViewModelProvider(context = LocalContext.current).factory
    ),
    hasNotificationPermission: Boolean = false,
    requestNotificationPermission: () -> Unit = {},
    scheduleNotification: (Long) -> Unit = {}
) {
    val boxScreenState = boxScreenViewModel.boxScreenState
    val tagSortedBy by boxScreenViewModel.tagSortedBy.collectAsState()
    val levelSelected by boxScreenViewModel.levelSelected.collectAsState()
    val trainingCounts by boxScreenViewModel.trainingCounts.collectAsState()
    val boxWithTags by boxScreenViewModel.boxWithTags.collectAsState()
    val cardsWithTags by boxScreenViewModel.cardsWithTags.collectAsState()

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
                    showCard = { cardDialog = true },
                    showEditCardDialog = { editCardDialog = true },
                    showNewTagDialog = { showNewTagDialog() },
                    showEditTagDialog = { showEditTagDialog() },
                    levelSelected = levelSelected,
                    boxWithTags = boxWithTags,
                    cardsWithTags = cardsWithTags,
                    filteredCardWithTagList = filteredCardWithTagList,
                    boxScreenViewModel = boxScreenViewModel,
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
            showEditCardDialog = { editCardDialog = true },
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
            onDismiss = { editCardDialog = false },
            boxWithTags = boxWithTags,
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
            onDismiss = { deleteCardDialog = false },
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