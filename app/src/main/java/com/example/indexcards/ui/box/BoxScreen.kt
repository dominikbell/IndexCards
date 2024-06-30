package com.example.indexcards.ui.box

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.R
import com.example.indexcards.data.Box
import com.example.indexcards.data.Tag
import com.example.indexcards.ui.card.CardDialog
import com.example.indexcards.ui.card.DeleteCardDialog
import com.example.indexcards.ui.card.EditCardDialog
import com.example.indexcards.ui.card.NewCardDialog
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
    boxScreenViewModel: BoxScreenViewModel,
    editCardViewModel: EditCardViewModel = viewModel(
        factory = ViewModelProvider(context = LocalContext.current).factory
    ),
) {
    val boxScreenState = boxScreenViewModel.boxScreenState
    val tagSortedBy: State<Tag> = boxScreenViewModel.tagSortedBy.collectAsState()
    val levelSelected = boxScreenViewModel.levelSelected.collectAsState()
    val boxWithTags = boxScreenViewModel.boxWithTags.collectAsState()
    val cardsWithTags = boxScreenViewModel.cardsWithTags.collectAsState()

    val filteredCardWithTagList =
        if (levelSelected.value == -1) {
            if (tagSortedBy.value == emptyTag) {
                cardsWithTags.value.cardWithTagList
            } else {
                cardsWithTags.value.cardWithTagList.filter { it.tags.contains(tagSortedBy.value) }
            }
        } else {
            if (tagSortedBy.value == emptyTag) {
                cardsWithTags.value.cardWithTagList.filter { it.card.level == levelSelected.value }
            } else {
                cardsWithTags.value.cardWithTagList.filter {
                    it.card.level == levelSelected.value && it.tags.contains(tagSortedBy.value)
                }
            }
        }

    var cardDialog by remember { mutableStateOf(false) }
    var newCardDialog by remember { mutableStateOf(false) }
    var editCardDialog by remember { mutableStateOf(false) }
    var deleteCardDialog by remember { mutableStateOf(false) }
    var newTag by remember { mutableStateOf(true) }
    var tagDialog by remember { mutableStateOf(false) }
    var deleteDialog by remember { mutableStateOf(false) }

    fun hideCardDialogs() {
        cardDialog = false; editCardDialog = false
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
            BoxTopBar(
                navigateToBoxesOverview = navigateToBoxesOverview,
                updateEditUiStatus = {
                    boxScreenViewModel.updateUiState(boxWithTags.value.box.toBoxDetails())
                },
                changeBoxScreenState = { boxScreenViewModel.changeBoxScreenState(it) },
                boxScreenState = boxScreenState,
                thisBox = boxWithTags.value.box,
                cancelEdit = { boxScreenViewModel.changeBoxScreenState(BoxScreenState.VIEW) }
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
                    levelSelected = levelSelected.value,
                    boxWithTags = boxWithTags.value,
                    cardsWithTags = cardsWithTags.value,
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
                )
            }

            BoxScreenState.TRAIN -> {
                TrainingScreen(
                    modifier = modifier.padding(innerPadding),
                    navigateToBoxScreen = { boxScreenViewModel.changeBoxScreenState(BoxScreenState.VIEW) },
                    cardList = filteredCardWithTagList.shuffled(),
                    onCardCorrect = {},
                    onCardIncorrect = {}
                )
            }
        }
    }

    if (cardDialog) {
        CardDialog(
            onDismiss = { hideCardDialogs() },
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
            onDismiss = { hideCardDialogs() },
            boxWithTags = boxWithTags.value,
            showCardDialog = { editCardDialog = false },
            showDeleteCard = {
                hideCardDialogs()
                deleteCardDialog = true
            },
            showNewTagDialog = { showNewTagDialog() },
            showEditTagDialog = { showEditTagDialog() },
        )
    }

    if (newCardDialog) {
        NewCardDialog(
            onDismiss = { newCardDialog = false },
            boxWithTags = boxWithTags.value,
            showNewTagDialog = { showNewTagDialog() },
            showEditTagDialog = { showEditTagDialog() },
        )
    }

    if (deleteCardDialog) {
        DeleteCardDialog(
            onDismiss = {
                cardDialog = false
                deleteCardDialog = false
            },
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
            onDismiss = {
                deleteDialog = false
            },
            onDelete = {
                navigateToBoxesOverview()
                boxScreenViewModel.deleteBox()
            },
            boxToBeDeleted = boxWithTags.value.box
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxTopBar(
    modifier: Modifier = Modifier,
    navigateToBoxesOverview: () -> Unit,
    updateEditUiStatus: () -> Unit,
    changeBoxScreenState: (BoxScreenState) -> Unit,
    boxScreenState: BoxScreenState,
    thisBox: Box,
    cancelEdit: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    val title: String =
        when (boxScreenState) {
            BoxScreenState.VIEW -> {
                thisBox.name
            }

            BoxScreenState.EDIT -> {
                "Editing " + thisBox.name
            }

            BoxScreenState.TRAIN -> {
                "Training"
            }
        }

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        navigationIcon = {
            if (boxScreenState == BoxScreenState.EDIT) {
                IconButton(
                    onClick = { cancelEdit() }
                ) {
                    Icon(imageVector = Icons.Filled.Clear, contentDescription = "Cancel")
                }
            } else {
                IconButton(onClick = {
                    if (boxScreenState == BoxScreenState.TRAIN) {
                        changeBoxScreenState(BoxScreenState.VIEW)
                    } else {
                        navigateToBoxesOverview()
                    }
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back Arrow"
                    )
                }
            }
        },

        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                modifier = modifier
            )
        },

        actions = {
            if (boxScreenState == BoxScreenState.VIEW) {
                IconButton(onClick = {
                    expanded = true
                }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Menu"
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(text = stringResource(R.string.edit_box))
                        },
                        onClick = {
                            updateEditUiStatus()
                            expanded = false
                            changeBoxScreenState(BoxScreenState.EDIT)
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(text = "Sort by")

                                Icon(
                                    imageVector = Icons.Filled.ArrowDropDown,
                                    modifier = Modifier.rotate(-90f),
                                    contentDescription = "sort by"
                                )
                            }
                        },
                        onClick = { /* TODO: implement sorting */ }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text = stringResource(R.string.train_all))
                        },
                        onClick = {
                            expanded = false
                            changeBoxScreenState(BoxScreenState.TRAIN)
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text = stringResource(R.string.train_selection))
                        },
                        onClick = {
                            expanded = false
                            changeBoxScreenState(BoxScreenState.TRAIN)
                        }
                    )
                }
            }
        },
    )
}
