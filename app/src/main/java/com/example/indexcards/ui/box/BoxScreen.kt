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
import com.example.indexcards.ui.card.CardDialog
import com.example.indexcards.ui.card.DeleteCardDialog
import com.example.indexcards.ui.card.EditCardDialog
import com.example.indexcards.ui.card.NewCardDialog
import com.example.indexcards.ui.tag.TagDialog
import com.example.indexcards.utils.ViewModelProvider
import com.example.indexcards.utils.box.BoxScreenViewModel
import com.example.indexcards.utils.box.toBoxDetails
import com.example.indexcards.utils.card.EditCardViewModel
import kotlinx.coroutines.launch

@Composable
fun BoxScreen(
    modifier: Modifier = Modifier,
    navigateToBoxesOverview: () -> Unit,
    navigateToTrainingScreen: (Long) -> Unit,
    boxId: Long,
    boxScreenViewModel: BoxScreenViewModel,
    editCardViewModel: EditCardViewModel = viewModel(
        factory = ViewModelProvider(context = LocalContext.current).factory
    ),
) {
    val boxWithCards = boxScreenViewModel.boxWithCards.collectAsState()
    val boxWithTags = boxScreenViewModel.boxWithTags.collectAsState()

    var isEditing by remember { mutableStateOf(false) }
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
        if (isEditing) {
            isEditing = false
        } else {
            navigateToBoxesOverview()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            BoxTopBar(
                navigateToBoxesOverview = navigateToBoxesOverview,
                editBox = {
                    boxScreenViewModel.updateUiState(boxWithCards.value.box.toBoxDetails())
                    isEditing = true
                },
                trainAll = {
                    navigateToTrainingScreen(boxId)
                },
                trainSelected = {
                    navigateToTrainingScreen(boxId)
                },
                thisBox = boxWithCards.value.box,
                isEditing = isEditing,
                cancelEdit = { isEditing = false }
            )
        },

        floatingActionButton = {
            if (isEditing) {
                FloatingActionButton(
                    onClick = { deleteDialog = true }
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            } else {
                FloatingActionButton(
                    onClick = { newCardDialog = true }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }
    ) { innerPadding ->
        if (isEditing) {
            BoxScreenEditing(
                modifier = modifier
                    .padding(innerPadding),
                onSave = {
                    isEditing = false
                    boxScreenViewModel.saveBox()
                },
                boxScreenViewModel = boxScreenViewModel,
            )
        } else {
            BoxScreenBody(
                modifier = modifier
                    .padding(innerPadding),
                showCard = { cardDialog = true },
                showEditCardDialog = { editCardDialog = true },
                showNewTagDialog = { showNewTagDialog() },
                showEditTagDialog = { showEditTagDialog() },
                boxScreenViewModel = boxScreenViewModel,
            )
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
            boxToBeDeleted = boxWithCards.value.box
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxTopBar(
    modifier: Modifier = Modifier,
    navigateToBoxesOverview: () -> Unit,
    editBox: () -> Unit,
    trainAll: () -> Unit,
    trainSelected: () -> Unit,
    thisBox: Box,
    isEditing: Boolean,
    cancelEdit: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        navigationIcon = {
            if (!isEditing) {
                IconButton(onClick = { navigateToBoxesOverview() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back Arrow"
                    )
                }
            } else {
                IconButton(
                    onClick = { cancelEdit() }
                ) {
                    Icon(imageVector = Icons.Filled.Clear, contentDescription = "Cancel")
                }
            }
        },

        title = {
            Text(
                text = thisBox.name,
                fontWeight = FontWeight.Bold,
                modifier = modifier
            )
        },

        actions = {
            if (!isEditing) {
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
                            expanded = false
                            editBox()
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
                            Text(text = "Train all")
                        },
                        onClick = {
                            expanded = false
                            trainAll()
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text = "Train current selection")
                        },
                        onClick = {
                            expanded = false
                            trainSelected()
                        }
                    )
                }
            }
        },
    )
}
