package com.example.indexcards.ui.home.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import com.example.indexcards.R
import com.example.indexcards.data.Box
import com.example.indexcards.data.LanguageData
import com.example.indexcards.utils.getCutString
import com.example.indexcards.utils.state.emptyBox


@Composable
fun MergeBoxesDialog(
    modifier: Modifier = Modifier,
    selectedBoxes: List<Box>,
    onDismiss: () -> Unit = {},
    onFinish: (Boolean, String, Boolean, Boolean, Boolean) -> Unit = { _, _, _, _, _ -> },
) {
    val topics = selectedBoxes.map { Pair(it.topic, it.boxId) }
    val haveSameTopics = topics.all { it.first == topics.first().first }

    var step by remember { mutableIntStateOf(0) }

    /* Screen 0 */
    var deleteOldBoxes by remember { mutableStateOf(false) }

    /* for choices on Screen 1 and Screen 2 */
    var noOptionChosen by remember { mutableStateOf(false) }

    /* Screen 1 */
    var newBoxName by remember { mutableStateOf("") }
    var customBoxName by remember { mutableStateOf("") }
    var selectedBoxIdName by remember { mutableLongStateOf(-2) }
    var validCustomName by remember { mutableStateOf(true) }

    /* Screen 2 */
    var newTopic by remember { mutableStateOf("") }
    var customTopic by remember { mutableStateOf("") }
    var selectedBoxIdTopic by remember { mutableLongStateOf(-2) }
    var validCustomTopic by remember { mutableStateOf(true) }

    /* Screen 3 */
    var newDescription by remember { mutableStateOf("") }

    /* Screen 4 */
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

                /** BoxName */
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
                                            selectedBoxIdName = it.boxId
                                        },
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    RadioButton(
                                        selected = (selectedBoxIdName == it.boxId),
                                        onClick = {
                                            validCustomName = true
                                            newBoxName = it.name
                                            selectedBoxIdName = it.boxId
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
                                    selected = (selectedBoxIdName == (-1).toLong()),
                                    onClick = {
                                        noOptionChosen = false
                                        newBoxName = customBoxName
                                        selectedBoxIdName = -1
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
                                        selectedBoxIdName = -1
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

                /** Topic */
                2 -> {
                    /* If all boxes have the same topic */
                    if (haveSameTopics) {
                        Column {
                            Text(text = stringResource(id = R.string.merge_boxes_have_same_name))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        noOptionChosen = false
                                        validCustomTopic = true
                                        newTopic = topics.first().first
                                        selectedBoxIdTopic = topics.first().second
                                    },
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                RadioButton(
                                    selected = newTopic == topics.first().first,
                                    onClick = {
                                        noOptionChosen = false
                                        validCustomTopic = true
                                        newTopic = topics.first().first
                                        selectedBoxIdTopic = topics.first().second
                                    },
                                )

                                Text(text = stringResource(id = R.string.keep_topic) + ": ")

                                Text(
                                    text = topics.first().first,
                                    fontStyle = FontStyle.Italic
                                )
                            }


                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                RadioButton(
                                    selected = (selectedBoxIdTopic == (-1).toLong()),
                                    onClick = {
                                        noOptionChosen = false
                                        newTopic = customTopic
                                        selectedBoxIdTopic = -1
                                    },
                                    colors = RadioButtonDefaults.colors().copy(
                                        unselectedColor =
                                        if (noOptionChosen) MaterialTheme.colorScheme.error
                                        else RadioButtonDefaults.colors().unselectedColor
                                    )
                                )
                                OutlinedTextField(
                                    value = customTopic,
                                    onValueChange = {
                                        validCustomTopic = true
                                        noOptionChosen = false
                                        customTopic = it
                                        newTopic = it
                                        selectedBoxIdTopic = -1
                                    },
                                    isError = !validCustomTopic,
                                    trailingIcon = {
                                        if (customTopic.isNotBlank()) {
                                            Icon(
                                                modifier = Modifier.clickable {
                                                    customTopic = ""
                                                },
                                                imageVector = Icons.Default.Clear,
                                                contentDescription = "clear"
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    } else {
                        Column {
                            Text(
                                modifier = Modifier.padding(bottom = 4.dp),
                                text = stringResource(id = R.string.merge_boxes_choose_name)
                            )

                            Column(
                                verticalArrangement = Arrangement.spacedBy(0.dp)
                            ) {
                                topics.distinct().forEach {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                validCustomTopic = true
                                                newTopic = it.first
                                                selectedBoxIdTopic = it.second
                                            },
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        RadioButton(
                                            selected = (selectedBoxIdTopic == it.second),
                                            onClick = {
                                                validCustomTopic = true
                                                newTopic = it.first
                                                selectedBoxIdTopic = it.second
                                            },
                                            colors = RadioButtonDefaults.colors().copy(
                                                unselectedColor =
                                                if (noOptionChosen) MaterialTheme.colorScheme.error
                                                else RadioButtonDefaults.colors().unselectedColor
                                            )
                                        )

                                        Text(text = it.first)
                                    }
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    RadioButton(
                                        selected = (selectedBoxIdTopic == (-1).toLong()),
                                        onClick = {
                                            noOptionChosen = false
                                            newTopic = customTopic
                                            selectedBoxIdTopic = -1
                                        },
                                        colors = RadioButtonDefaults.colors().copy(
                                            unselectedColor =
                                            if (noOptionChosen) MaterialTheme.colorScheme.error
                                            else RadioButtonDefaults.colors().unselectedColor
                                        )
                                    )
                                    OutlinedTextField(
                                        value = customTopic,
                                        onValueChange = {
                                            validCustomTopic = true
                                            noOptionChosen = false
                                            customTopic = it
                                            newTopic = it
                                            selectedBoxIdTopic = -1
                                        },
                                        isError = !validCustomTopic,
                                        trailingIcon = {
                                            if (customTopic.isNotBlank()) {
                                                Icon(
                                                    modifier = Modifier.clickable {
                                                        customTopic = ""
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
                }

                /** Description */
                3 -> {
                    Column {
                        Text(
                            modifier = Modifier.padding(bottom = 6.dp),
                            text = stringResource(id = R.string.merge_boxes_description),
                        )

                        OutlinedTextField(
                            value = newDescription,
                            onValueChange = { newDescription = it },
                            minLines = 3,
                            maxLines = 5,
                            trailingIcon = {
                                if (newDescription.isNotBlank()) {
                                    Icon(
                                        modifier = Modifier.clickable {
                                            newDescription = ""
                                        },
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "clear"
                                    )
                                }
                            }
                        )
                    }
                }

                /** Decide if cards, tags, and categories should be transferred  */
                4 -> {
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
                else -> {
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

                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (LanguageData.language.values.any { it.first == newTopic }) {
                                Text(text = stringResource(id = R.string.language) + ": ")
                            } else {
                                Text(text = stringResource(id = R.string.topic) + ": ")
                            }
                            Text(text = newTopic.getCutString(25))
                        }

                        if (newDescription.isNotBlank()) {
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = stringResource(id = R.string.description) + ": ")
                                Text(text = newDescription.getCutString(25))
                            }
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
                            HorizontalDivider(
                                modifier = Modifier.padding(
                                    top = 10.dp,
                                    bottom = 10.dp
                                )
                            )

                            Text(
                                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                                text = stringResource(id = R.string.old_boxes_will_be_deleted) + " " +
                                        stringResource(id = R.string.action_cannot_be_undone)
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

                if ((0..4).contains(step)) {
                    TextButton(
                        onClick = {
                            if (step == 1) {
                                /* If no option was chosen */
                                if (selectedBoxIdName == (-2).toLong()) {
                                    noOptionChosen = true
                                    return@TextButton
                                }
                                /* If a custom name was chosen but the TextField is blank */
                                if (selectedBoxIdName == (-1).toLong() && customBoxName.isBlank()) {
                                    validCustomName = false
                                    return@TextButton
                                }
                            }
                            if (step == 2) {
                                /* If no option was chosen */
                                if (selectedBoxIdTopic == (-2).toLong()) {
                                    noOptionChosen = true
                                    return@TextButton
                                }
                                /* If a custom name was chosen but the TextField is blank */
                                if (selectedBoxIdTopic == (-1).toLong() && customTopic.isBlank()) {
                                    validCustomTopic = false
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
    val box1 = emptyBox.copy(name = "Box1", topic = "test")
    val box2 = emptyBox.copy(name = "Another Box", topic = "test")
    val box3 = emptyBox.copy(name = "Spanish B1", topic = "test1")
    MergeBoxesDialog(
        selectedBoxes = listOf(box1, box2, box3)
    )
}