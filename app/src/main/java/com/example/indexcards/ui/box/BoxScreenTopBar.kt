package com.example.indexcards.ui.box

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.indexcards.R
import com.example.indexcards.data.Box
import com.example.indexcards.ui.elements.BoxNameWithFlag
import com.example.indexcards.utils.box.BoxScreenSorting
import com.example.indexcards.utils.box.BoxScreenState
import com.example.indexcards.utils.box.UiBoxWithTags
import com.example.indexcards.utils.box.boxScreenSorting
import com.example.indexcards.utils.state.emptyBox
import com.example.indexcards.utils.state.isLanguage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxScreenTopBar(
    modifier: Modifier = Modifier,
    boxWithTags: UiBoxWithTags,
    boxScreenState: BoxScreenState,
    trainingCounts: Boolean,
    allCategoriesCollapsed: Boolean,
    isSelecting: Boolean,
    navigateToBoxesOverview: () -> Unit = {},
    updateEditUiStatus: () -> Unit = {},
    changeBoxScreenState: (BoxScreenState) -> Unit = {},
    cancelEdit: () -> Unit = {},
    changeTrainingCounts: () -> Unit = {},
    changeTrainingDirection: () -> Unit = {},
    changeTrainingDirectionToValue: (Boolean) -> Unit = {},
    exportBox: () -> Unit = {},
    showSearch: () -> Unit = {},
    onSortBy: (BoxScreenSorting) -> Unit = {},
    setRemindersAfterTraining: () -> Unit = {},
    setTrainSelection: (Boolean) -> Unit = {},
    toggleAllCategories: () -> Unit = {},
    stopSelection: () -> Unit = {},
    showTagsToCardsDialog: () -> Unit = {},
    showCardsToCategoryDialog: () -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }
    var sortExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = boxScreenState) {
        expanded = false
    }

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        navigationIcon = {
            when (boxScreenState) {
                BoxScreenState.EDIT -> {
                    IconButton(
                        onClick = { cancelEdit() }
                    ) {
                        Icon(imageVector = Icons.Filled.Clear, contentDescription = "Cancel")
                    }
                }

                BoxScreenState.TRAIN -> {
                    IconButton(
                        onClick = {
                            setRemindersAfterTraining()
                            changeBoxScreenState(BoxScreenState.VIEW)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back Arrow"
                        )
                    }
                }

                BoxScreenState.VIEW -> {
                    if (isSelecting) {
                        IconButton(
                            onClick = { stopSelection() }
                        ) {
                            Icon(imageVector = Icons.Filled.Clear, contentDescription = "Cancel")
                        }
                    } else {
                        IconButton(
                            onClick = {
                                navigateToBoxesOverview()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back Arrow"
                            )
                        }

                    }
                }
            }
        },

        title = {
            when (boxScreenState) {
                BoxScreenState.VIEW -> {
                    if (isSelecting) {
                        Text(
                            text = stringResource(id = R.string.editing),
                            fontWeight = FontWeight.Bold,
                            modifier = modifier
                        )
                    } else {
                        BoxNameWithFlag(box = boxWithTags.box, doBold = true, isTitle = false)
                    }
                }

                BoxScreenState.EDIT -> {
                    Text(
                        text = stringResource(id = R.string.editing_box) + " " + boxWithTags.box.name,
                        fontWeight = FontWeight.Bold,
                        modifier = modifier
                    )
                }

                BoxScreenState.TRAIN -> {
                    Text(
                        text = stringResource(id = R.string.training),
                        fontWeight = FontWeight.Bold,
                        modifier = modifier
                    )
                }
            }
        },

        actions = {
            when (boxScreenState) {
                BoxScreenState.VIEW -> {
                    if (!isSelecting) {
                        IconButton(
                            onClick = { expanded = true }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = "Menu"
                            )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            /** Edit Box Details */
                            DropdownMenuItem(
                                text = { Text(text = stringResource(R.string.edit_box)) },
                                onClick = {
                                    updateEditUiStatus()
                                    expanded = false
                                    changeBoxScreenState(BoxScreenState.EDIT)
                                }
                            )

                            /** Search */
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                    ) {
                                        Text(text = stringResource(R.string.search))

                                        Icon(
                                            imageVector = Icons.Default.Search,
                                            contentDescription = "search"
                                        )
                                    }
                                },
                                onClick = {
                                    expanded = false
                                    showSearch()
                                }
                            )

                            /** Export to CSV*/
                            DropdownMenuItem(
                                text = { Text(text = stringResource(R.string.export_box)) },
                                onClick = {
                                    expanded = false
                                    exportBox()
                                }
                            )

                            /** Sorting */
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Text(text = stringResource(id = R.string.sort_by))

                                        Icon(
                                            imageVector = Icons.Filled.ArrowDropDown,
                                            modifier = Modifier.rotate(-90f),
                                            contentDescription = "sort by"
                                        )
                                    }
                                },
                                onClick = {
                                    expanded = false
                                    sortExpanded = true
                                }
                            )

                            /** Expand/Collapse categories */
                            if (allCategoriesCollapsed) {
                                DropdownMenuItem(
                                    text = { Text(text = stringResource(R.string.expand_all_categories)) },
                                    onClick = {
                                        expanded = false
                                        toggleAllCategories()
                                    }
                                )
                            } else {
                                DropdownMenuItem(
                                    text = { Text(text = stringResource(R.string.collapse_all_categories)) },
                                    onClick = {
                                        expanded = false
                                        toggleAllCategories()
                                    }
                                )
                            }

                            /** Train all */
                            DropdownMenuItem(
                                text = { Text(text = stringResource(R.string.train_all)) },
                                onClick = {
                                    expanded = false
                                    setTrainSelection(false)
                                    changeTrainingDirectionToValue(true)
                                    changeBoxScreenState(BoxScreenState.TRAIN)
                                }
                            )

                            /** Train selection */
                            DropdownMenuItem(
                                text = { Text(text = stringResource(R.string.train_selection)) },
                                onClick = {
                                    expanded = false
                                    setTrainSelection(true)
                                    changeTrainingDirectionToValue(true)
                                    changeBoxScreenState(BoxScreenState.TRAIN)
                                }
                            )
                        }

                        /** Sorting Menu */
                        DropdownMenu(
                            expanded = sortExpanded,
                            onDismissRequest = { sortExpanded = false }
                        ) {
                            boxScreenSorting.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(text = stringResource(option.second)) },
                                    onClick = {
                                        sortExpanded = false
                                        onSortBy(option.first)
                                    }
                                )
                            }
                        }
                    } else {
                        if (boxWithTags.box.categories || boxWithTags.tagList.isNotEmpty()) {
                            IconButton(
                                onClick = { expanded = true }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.MoreVert,
                                    contentDescription = "Menu"
                                )
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                if (boxWithTags.tagList.isNotEmpty()) {
                                    DropdownMenuItem(
                                        text = { Text(text = stringResource(R.string.edit_tags)) },
                                        onClick = {
                                            showTagsToCardsDialog()
                                            expanded = false
                                        }
                                    )
                                }
                                if (boxWithTags.box.categories) {
                                    DropdownMenuItem(
                                        text = { Text(text = stringResource(R.string.add_category)) },
                                        onClick = {
                                            showCardsToCategoryDialog()
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                BoxScreenState.EDIT -> {}

                BoxScreenState.TRAIN -> {
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
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Text(text = stringResource(id = R.string.training_counts))
                                    Checkbox(
                                        checked = trainingCounts,
                                        onCheckedChange = { changeTrainingCounts() }
                                    )
                                }
                            },
                            onClick = { changeTrainingCounts() }
                        )
                        if (boxWithTags.box.isLanguage()) {
                            DropdownMenuItem(
                                text = {
                                    Text(text = stringResource(id = R.string.reverse_sides))
                                },
                                onClick = {
                                    expanded = false
                                    changeTrainingDirection()
                                }
                            )
                        }
                    }
                }
            }
        },
    )
}

@Preview
@Composable
fun BoxTopBarViewPreview() {
    BoxScreenTopBar(
        boxScreenState = BoxScreenState.VIEW,
        boxWithTags = UiBoxWithTags(
            box = emptyBox.copy(name = "Test123", topic = "English"),
            tagList = listOf()
        ),
        trainingCounts = false,
        allCategoriesCollapsed = true,
        isSelecting = false,
    )
}

@Preview
@Composable
fun BoxTopBarViewSelectingPreview() {
    BoxScreenTopBar(
        boxScreenState = BoxScreenState.VIEW,
        boxWithTags = UiBoxWithTags(
            box = emptyBox.copy(name = "Test123", topic = "English"),
            tagList = listOf()
        ),
        trainingCounts = false,
        allCategoriesCollapsed = true,
        isSelecting = true,
    )
}

@Preview
@Composable
fun BoxTopBarTrainPreview() {
    BoxScreenTopBar(
        boxScreenState = BoxScreenState.TRAIN,
        boxWithTags = UiBoxWithTags(box = emptyBox.copy(name = "Test123"), tagList = listOf()),
        trainingCounts = true,
        allCategoriesCollapsed = true,
        isSelecting = false,
    )
}

@Preview
@Composable
fun BoxTopBarEditPreview() {
    BoxScreenTopBar(
        boxScreenState = BoxScreenState.EDIT,
        boxWithTags = UiBoxWithTags(box = emptyBox.copy(name = "Test123"), tagList = listOf()),
        trainingCounts = false,
        allCategoriesCollapsed = true,
        isSelecting = false,
    )
}