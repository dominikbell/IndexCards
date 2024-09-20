package com.example.indexcards.ui.home.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Merge
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.example.indexcards.R
import com.example.indexcards.data.Box
import com.example.indexcards.utils.state.emptyBox


@Composable
fun MergeBoxesDialog(
    modifier: Modifier = Modifier,
    selectedBoxes: List<Box>,
    onDismiss: () -> Unit = {},
    onFinish: (Boolean, String, Boolean, Boolean, Boolean) -> Unit = { _, _, _, _, _ -> },
) {
    var step by remember { mutableIntStateOf(3) }

    /* Screen 0 */
    var deleteOldBoxes by remember { mutableStateOf(true) }

    /* Screen 1 */
    var newBoxName by remember { mutableStateOf("") }
    var customBoxName by remember { mutableStateOf("") }
    var validCustomName by remember { mutableStateOf(true) }
    var noOptionChosen by remember { mutableStateOf(false) }
    var selectedBoxId by remember { mutableLongStateOf(-2) }

    /* Screen 2 */
    var transferCards by remember { mutableStateOf(true) }
    var transferTags by remember { mutableStateOf(true) }
    var transferCategories by remember { mutableStateOf(true) }

    AlertDialog(
        title = {
            Text(text = stringResource(id = R.string.merge_boxes))
        },
        text = {
            when (step) {
                /** List boxes that will be merged
                 * Decide if old boxes should be deleted
                 */
                0 -> {
                    Column {
                        Text(text = stringResource(id = R.string.merge_boxes_names))

                        selectedBoxes.forEach {
                            Text(
                                modifier = Modifier.padding(start = 10.dp),
                                text = "\u2022 " + it.name,
                                fontStyle = FontStyle.Italic
                            )
                        }


                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text = stringResource(id = R.string.merge_boxes_explanation)
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { deleteOldBoxes = !deleteOldBoxes }
                        ) {
                            Checkbox(
                                checked = deleteOldBoxes,
                                onCheckedChange = { deleteOldBoxes = it })
                            Text(text = stringResource(id = R.string.merge_boxes_delete))
                        }

                        if (deleteOldBoxes) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(id = R.string.action_cannot_be_undone),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                /** Choose the name */
                1 -> {
                    Column {
                        Text(
                            modifier = Modifier.padding(bottom = 4.dp),
                            text = stringResource(id = R.string.merge_boxes_choose_name)
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(0.dp)
                        ) {
                            selectedBoxes.forEach {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            validCustomName = true
                                            newBoxName = it.name
                                            selectedBoxId = it.boxId
                                        },
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    RadioButton(
                                        selected = (selectedBoxId == it.boxId),
                                        onClick = {
                                            validCustomName = true
                                            newBoxName = it.name
                                            selectedBoxId = it.boxId
                                        },
                                        colors = RadioButtonDefaults.colors().copy(
                                            unselectedColor =
                                            if (noOptionChosen) MaterialTheme.colorScheme.error
                                            else RadioButtonDefaults.colors().unselectedColor
                                        )
                                    )

                                    Text(text = it.name)
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                RadioButton(
                                    selected = (selectedBoxId == (-1).toLong()),
                                    onClick = {
                                        noOptionChosen = false
                                        newBoxName = customBoxName
                                        selectedBoxId = -1
                                    },
                                    colors = RadioButtonDefaults.colors().copy(
                                        unselectedColor =
                                        if (noOptionChosen) MaterialTheme.colorScheme.error
                                        else RadioButtonDefaults.colors().unselectedColor
                                    )
                                )
                                OutlinedTextField(
                                    value = customBoxName,
                                    onValueChange = {
                                        validCustomName = true
                                        noOptionChosen = false
                                        customBoxName = it
                                        newBoxName = it
                                        selectedBoxId = -1
                                    },
                                    isError = !validCustomName,
                                    trailingIcon = {
                                        if (customBoxName.isNotBlank()) {
                                            Icon(
                                                modifier = Modifier.clickable {
                                                    customBoxName = ""
                                                },
                                                imageVector = Icons.Default.Clear,
                                                contentDescription = "clear"
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                /** Decide if cards, tags, and categories should be transferred  */
                2 -> {
                    Column {
                        Text(text = stringResource(id = R.string.merge_boxes_choose_imports))

                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { transferCards = !transferCards },
                            ) {
                                Checkbox(
                                    checked = transferCards,
                                    onCheckedChange = { transferCards = it }
                                )

                                Text(text = stringResource(id = R.string.keep_all_cards))
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { transferTags = !transferTags },
                            ) {
                                Checkbox(
                                    checked = transferTags,
                                    onCheckedChange = { transferTags = it }
                                )

                                Text(text = stringResource(id = R.string.keep_all_tags))
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { transferCategories = !transferCategories },
                            ) {
                                Checkbox(
                                    checked = transferCategories,
                                    onCheckedChange = { transferCategories = it }
                                )

                                Text(text = stringResource(id = R.string.keep_all_categories))
                            }
                        }
                    }

                }

                /** List all choices and confirm */
                3 -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column {
                                selectedBoxes.forEach {
                                    Text(text = it.name)
                                }
                            }

                            Icon(
                                modifier = Modifier.rotate(90F),
                                imageVector = Icons.Filled.Merge,
                                contentDescription = "merge"
                            )

                            Text(text = newBoxName)
                        }

                        HorizontalDivider(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))

                        Column(
                            modifier = Modifier
                                .padding(start = 10.dp, end = 10.dp)
                                .fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    modifier = Modifier.weight(1F),
                                    text = stringResource(id = R.string.keep_all_cards) + ":"
                                )
                                if (transferCards) {
                                    Text(text = stringResource(id = R.string.yes))
                                } else {
                                    Text(text = stringResource(id = R.string.no))
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    modifier = Modifier.weight(1F),
                                    text = stringResource(id = R.string.keep_all_tags) + ":"
                                )
                                if (transferTags) {
                                    Text(text = stringResource(id = R.string.yes))
                                } else {
                                    Text(text = stringResource(id = R.string.no))
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    modifier = Modifier.weight(1F),
                                    text = stringResource(id = R.string.keep_all_categories) + ":"
                                )
                                if (transferCategories) {
                                    Text(text = stringResource(id = R.string.yes))
                                } else {
                                    Text(text = stringResource(id = R.string.no))
                                }
                            }
                        }

                        if (deleteOldBoxes) {
                            HorizontalDivider(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))

                            Text(
                                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                                text = stringResource(id = R.string.old_boxes_will_be_deleted) + " " + stringResource(id = R.string.action_cannot_be_undone)
                            )
                        }
                    }
                }
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            Row {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text(
                        text = stringResource(id = R.string.cancel)
                    )
                }

                Spacer(modifier = Modifier.weight(1F))

                if (step != 0) {
                    TextButton(
                        onClick = { step -= 1 }
                    ) {
                        Text(text = stringResource(id = R.string.previous))
                    }
                }

                if (step != 3) {
                    TextButton(
                        onClick = {
                            if (step == 1) {
                                /* If no option was chosen */
                                if (selectedBoxId == (-2).toLong()) {
                                    noOptionChosen = true
                                    return@TextButton
                                }
                                /* If a custom name was chosen but the TextField is blank */
                                if (selectedBoxId == (-1).toLong() && customBoxName.isBlank()) {
                                    validCustomName = false
                                    return@TextButton
                                }
                            }

                            step += 1
                        }
                    ) {
                        Text(text = stringResource(id = R.string.next))
                    }
                } else {
                    TextButton(
                        onClick = {
                            onFinish(
                                deleteOldBoxes,
                                newBoxName,
                                transferCards,
                                transferTags,
                                transferCategories
                            )
                        }
                    ) {
                        Text(text = stringResource(id = R.string.finish))
                    }
                }
            }
        },
    )
}

@Preview
@Composable
fun MergeBoxesDialogPreview() {
    val box1 = emptyBox.copy(name = "Box1")
    val box2 = emptyBox.copy(name = "Another Box")
    val box3 = emptyBox.copy(name = "Spanish B1")
    MergeBoxesDialog(
        selectedBoxes = listOf(box1, box2, box3)
    )
}