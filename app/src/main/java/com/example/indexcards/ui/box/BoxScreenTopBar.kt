package com.example.indexcards.ui.box

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.indexcards.R
import com.example.indexcards.data.Box
import com.example.indexcards.data.isLanguage
import com.example.indexcards.ui.elements.BoxNameWithFlag
import com.example.indexcards.utils.box.BoxScreenState
import com.example.indexcards.utils.box.emptyBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxScreenTopBar(
    modifier: Modifier = Modifier,
    thisBox: Box,
    boxScreenState: BoxScreenState,
    trainingCounts: Boolean,
    navigateToBoxesOverview: () -> Unit = {},
    updateEditUiStatus: () -> Unit = {},
    changeBoxScreenState: (BoxScreenState) -> Unit = {},
    cancelEdit: () -> Unit = {},
    changeTrainingCounts: () -> Unit = {},
    changeTrainingDirection: () -> Unit = {},
    changeTrainingDirectionToValue: (Boolean) -> Unit = {},
    exportBox: () -> Unit = {},
    showSearch: () -> Unit = {},
) {
    val context = LocalContext.current
    val notImplementedText = stringResource(id = R.string.not_implemented)
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = boxScreenState) {
        expanded = false
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
            when (boxScreenState) {
                BoxScreenState.VIEW -> {
                    BoxNameWithFlag(box = thisBox, doBold = true, isTitle = false)
                }

                BoxScreenState.EDIT -> {
                    Text(
                        text = stringResource(id = R.string.editing_box) + " " + thisBox.name,
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
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.edit_box)) },
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
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.export_box)) },
                            onClick = {
                                expanded = false
                                exportBox()
                            }
                        )
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
                                /* TODO: implement sorting */
                                Toast.makeText(context, notImplementedText, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.train_all)) },
                            onClick = {
                                expanded = false
                                changeTrainingDirectionToValue(true)
                                changeBoxScreenState(BoxScreenState.TRAIN)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.train_selection)) },
                            onClick = {
                                expanded = false
                                changeTrainingDirectionToValue(true)
                                changeBoxScreenState(BoxScreenState.TRAIN)
                            }
                        )
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
                        if (thisBox.isLanguage()) {
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
fun BoxTopBarPreviewView() {
    BoxScreenTopBar(
        boxScreenState = BoxScreenState.VIEW,
        thisBox = emptyBox.copy(name = "Test123", topic = "English"),
        trainingCounts = false,
    )
}

@Preview
@Composable
fun BoxTopBarPreviewTrain() {
    BoxScreenTopBar(
        boxScreenState = BoxScreenState.TRAIN,
        thisBox = emptyBox.copy(name = "Test123"),
        trainingCounts = true,
    )
}

@Preview
@Composable
fun BoxTopBarPreviewEdit() {
    BoxScreenTopBar(
        boxScreenState = BoxScreenState.EDIT,
        thisBox = emptyBox.copy(name = "Test123"),
        trainingCounts = false,
    )
}