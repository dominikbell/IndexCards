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
    onFinish: (Boolean, String, String, String, Boolean, Boolean, Boolean, Boolean, Boolean) -> Unit = { _, _, _, _, _, _, _, _, _ -> },
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
    var keepLevels by remember { mutableStateOf(true) }
    var transferMemos by remember { mutableStateOf(true) }
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
                    MergeDialogScreen0(
                        selectedBoxes = selectedBoxes,
                        deleteOldBoxes = deleteOldBoxes,
                        onClick = { deleteOldBoxes = !deleteOldBoxes }
                    )
                }

                /** BoxName */
                1 -> {
                    MergeDialogScreen1(
                        selectedBoxes = selectedBoxes,
                        validCustomName = validCustomName,
                        selectedBoxIdName = selectedBoxIdName,
                        noOptionChosen = noOptionChosen,
                        customBoxName = customBoxName,
                        onClick = { box ->
                            validCustomName = true
                            noOptionChosen = false
                            newBoxName = box.name
                            selectedBoxIdName = box.boxId
                        },
                        typeCustomName = { newName ->
                            validCustomName = true
                            noOptionChosen = false
                            customBoxName = newName
                            newBoxName = newName
                            selectedBoxIdName = -1
                        }
                    )
                }

                /** Topic */
                2 -> {
                    MergeDialogScreen2(
                        topics = topics,
                        haveSameTopics = haveSameTopics,
                        noOptionChosen = noOptionChosen,
                        selectedBoxIdTopic = selectedBoxIdTopic,
                        newTopic = newTopic,
                        customTopic = customTopic,
                        validCustomTopic = validCustomTopic,
                        onClick = { pair ->
                            validCustomTopic = true
                            noOptionChosen = false
                            selectedBoxIdTopic = pair.second
                            newTopic = pair.first
                        },
                        typeCustomTopic = { topic ->
                            validCustomTopic = true
                            noOptionChosen = false
                            customTopic = topic
                            newTopic = topic
                            selectedBoxIdTopic = -1
                        }
                    )
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

                /** Decide if cards, tags, categories, memos, levels should be transferred  */
                4 -> {
                    MergeDialogScreen4(
                        transferCards = transferCards,
                        transferMemos = transferMemos,
                        keepLevels = keepLevels,
                        transferTags = transferTags,
                        transferCategories = transferCategories,
                        toggleTransferCards = { transferCards = !transferCards },
                        toggleTransferMemos = { transferMemos = !transferMemos },
                        toggleKeepLevels = { keepLevels = !keepLevels },
                        toggleTransferTags = { transferTags = !transferTags },
                        toggleTransferCategories = { transferCategories = !transferCategories },
                    )
                }

                /** List all choices and confirm */
                else -> {
                    MergeDialogScreen5(
                        selectedBoxes = selectedBoxes,
                        deleteOldBoxes = deleteOldBoxes,
                        newBoxName = newBoxName,
                        newTopic = newTopic,
                        newDescription = newDescription,
                        transferCards = transferCards,
                        transferTags = transferTags,
                        transferCategories = transferCategories,
                    )
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
                                newDescription,
                                newTopic,
                                transferCards,
                                transferTags,
                                transferCategories,
                                transferMemos,
                                keepLevels,
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


@Composable
fun MergeDialogScreen0(
    selectedBoxes: List<Box>,
    deleteOldBoxes: Boolean,
    onClick: () -> Unit = {},
) {
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
                .clickable { onClick() }
        ) {
            Checkbox(
                checked = deleteOldBoxes,
                onCheckedChange = { onClick() }
            )
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

@Preview(showBackground = true)
@Composable
fun MergeDialogScreen0Preview() {
    val box1 = emptyBox.copy(name = "Box1", topic = "test")
    val box2 = emptyBox.copy(name = "Another Box", topic = "test")
    val box3 = emptyBox.copy(name = "Spanish B1", topic = "test1")
    MergeDialogScreen0(
        selectedBoxes = listOf(box1, box2, box3),
        deleteOldBoxes = true
    )
}


@Composable
fun MergeDialogScreen1(
    selectedBoxes: List<Box>,
    validCustomName: Boolean,
    selectedBoxIdName: Long,
    noOptionChosen: Boolean,
    customBoxName: String,
    onClick: (Box) -> Unit = {},
    typeCustomName: (String) -> Unit = {},
) {
    Column {
        Text(
            modifier = Modifier.padding(bottom = 4.dp),
            text = stringResource(id = R.string.merge_boxes_choose_name)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            selectedBoxes.forEach { box ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onClick(box) },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = (selectedBoxIdName == box.boxId),
                        onClick = { onClick(box) },
                        colors = RadioButtonDefaults.colors().copy(
                            unselectedColor =
                            if (noOptionChosen) MaterialTheme.colorScheme.error
                            else RadioButtonDefaults.colors().unselectedColor
                        )
                    )

                    Text(text = box.name)
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = (selectedBoxIdName == (-1).toLong()),
                    onClick = { onClick(emptyBox.copy(name = customBoxName)) },
                    colors = RadioButtonDefaults.colors().copy(
                        unselectedColor =
                        if (noOptionChosen) MaterialTheme.colorScheme.error
                        else RadioButtonDefaults.colors().unselectedColor
                    )
                )
                OutlinedTextField(
                    value = customBoxName,
                    onValueChange = { typeCustomName(it) },
                    isError = !validCustomName,
                    trailingIcon = {
                        if (customBoxName.isNotBlank()) {
                            Icon(
                                modifier = Modifier.clickable { typeCustomName("") },
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

@Preview(showBackground = true)
@Composable
fun MergeDialogScreen1Preview() {
    val box1 = emptyBox.copy(name = "Box1", topic = "test")
    val box2 = emptyBox.copy(name = "Another Box", topic = "test")
    val box3 = emptyBox.copy(name = "Spanish B1", topic = "test1")
    MergeDialogScreen1(
        selectedBoxes = listOf(box1, box2, box3),
        validCustomName = true,
        selectedBoxIdName = (-1).toLong(),
        noOptionChosen = true,
        customBoxName = "NewName",
    )
}


@Composable
fun MergeDialogScreen2(
    topics: List<Pair<String, Long>>,
    haveSameTopics: Boolean,
    noOptionChosen: Boolean,
    selectedBoxIdTopic: Long,
    newTopic: String,
    customTopic: String,
    validCustomTopic: Boolean,
    onClick: (Pair<String, Long>) -> Unit = {},
    typeCustomTopic: (String) -> Unit = {},
) {
    /* If all boxes have the same topic */
    if (haveSameTopics) {
        Column {
            Text(text = stringResource(id = R.string.merge_boxes_have_same_name))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick(topics.first()) },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = (newTopic == topics.first().first),
                    onClick = { onClick(topics.first()) },
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
                    onClick = { onClick(Pair(customTopic, (-1).toLong())) },
                    colors = RadioButtonDefaults.colors().copy(
                        unselectedColor =
                        if (noOptionChosen) MaterialTheme.colorScheme.error
                        else RadioButtonDefaults.colors().unselectedColor
                    )
                )
                OutlinedTextField(
                    value = customTopic,
                    onValueChange = { typeCustomTopic(it) },
                    isError = !validCustomTopic,
                    trailingIcon = {
                        if (customTopic.isNotBlank()) {
                            Icon(
                                modifier = Modifier.clickable { typeCustomTopic("") },
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
                topics.distinct().forEach { topic ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onClick(topic) },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = (selectedBoxIdTopic == topic.second),
                            onClick = { onClick(topic) },
                            colors = RadioButtonDefaults.colors().copy(
                                unselectedColor =
                                if (noOptionChosen) MaterialTheme.colorScheme.error
                                else RadioButtonDefaults.colors().unselectedColor
                            )
                        )

                        Text(text = topic.first)
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = (selectedBoxIdTopic == (-1).toLong()),
                        onClick = { onClick(Pair(customTopic, (-1).toLong())) },
                        colors = RadioButtonDefaults.colors().copy(
                            unselectedColor =
                            if (noOptionChosen) MaterialTheme.colorScheme.error
                            else RadioButtonDefaults.colors().unselectedColor
                        )
                    )
                    OutlinedTextField(
                        value = customTopic,
                        onValueChange = { typeCustomTopic(it) },
                        isError = !validCustomTopic,
                        trailingIcon = {
                            if (customTopic.isNotBlank()) {
                                Icon(
                                    modifier = Modifier.clickable { typeCustomTopic("") },
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

@Preview(showBackground = true)
@Composable
fun MergeDialogScreen2Preview() {
    val box1 = emptyBox.copy(name = "Box1", topic = "test")
    val box2 = emptyBox.copy(name = "Another Box", topic = "test")
    val box3 = emptyBox.copy(name = "Spanish B1", topic = "test1")
    val boxes = listOf(box1, box2, box3)
    MergeDialogScreen2(
        topics = boxes.map { Pair(it.topic, it.boxId) },
        haveSameTopics = boxes.all { it.topic == box1.topic },
        noOptionChosen = false,
        selectedBoxIdTopic = (-1).toLong(),
        newTopic = "new",
        customTopic = "custom",
        validCustomTopic = true,
    )
}


@Composable
fun MergeDialogScreen4(
    transferCards: Boolean,
    transferMemos: Boolean,
    keepLevels: Boolean,
    transferTags: Boolean,
    transferCategories: Boolean,
    toggleTransferCards: () -> Unit = {},
    toggleTransferMemos: () -> Unit = {},
    toggleKeepLevels: () -> Unit = {},
    toggleTransferTags: () -> Unit = {},
    toggleTransferCategories: () -> Unit = {},
) {
    Column {
        Text(text = stringResource(id = R.string.merge_boxes_choose_imports))

        Column {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { toggleTransferCards() },
                ) {
                    Checkbox(
                        checked = transferCards,
                        onCheckedChange = { toggleTransferCards() },
                    )

                    Text(text = stringResource(id = R.string.keep_cards))
                }
                if (transferCards) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(start = 18.dp)
                            .fillMaxWidth()
                            .clickable { toggleTransferMemos() },
                    ) {
                        Checkbox(
                            checked = transferMemos,
                            onCheckedChange = { toggleTransferMemos() },
                        )

                        Text(text = stringResource(id = R.string.keep_memos))
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(start = 18.dp)
                            .fillMaxWidth()
                            .clickable { toggleKeepLevels() },
                    ) {
                        Checkbox(
                            checked = keepLevels,
                            onCheckedChange = { toggleKeepLevels() },
                        )

                        Text(text = stringResource(id = R.string.keep_levels))
                    }
                }
            }

            HorizontalDivider()

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { toggleTransferTags() },
            ) {
                Checkbox(
                    checked = transferTags,
                    onCheckedChange = { toggleTransferTags() },
                )

                Text(text = stringResource(id = R.string.keep_tags))
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { toggleTransferCategories() },
            ) {
                Checkbox(
                    checked = transferCategories,
                    onCheckedChange = { toggleTransferCategories() },
                )

                Text(text = stringResource(id = R.string.keep_categories))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MergeDialogScreen4Preview() {
    MergeDialogScreen4(
        transferCards = true,
        transferMemos = true,
        keepLevels = true,
        transferTags = true,
        transferCategories = true,
    )
}


@Composable
fun MergeDialogScreen5(
    selectedBoxes: List<Box>,
    deleteOldBoxes: Boolean,
    newBoxName: String,
    newTopic: String,
    newDescription: String,
    transferCards: Boolean,
    transferTags: Boolean,
    transferCategories: Boolean,
) {
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
                    text = stringResource(id = R.string.keep_cards) + ":"
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
                    text = stringResource(id = R.string.keep_tags) + ":"
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
                    text = stringResource(id = R.string.keep_categories) + ":"
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

@Preview(showBackground = true)
@Composable
fun MergeDialogScreen5Preview() {
    val box1 = emptyBox.copy(name = "Box1", topic = "test")
    val box2 = emptyBox.copy(name = "Another Box", topic = "test")
    val box3 = emptyBox.copy(name = "Spanish B1", topic = "test1")
    val boxes = listOf(box1, box2, box3)
    MergeDialogScreen5(
        selectedBoxes = boxes,
        deleteOldBoxes = true,
        newBoxName = "newname",
        newTopic = "topic",
        newDescription = "beschreibung",
        transferCards = true,
        transferTags = true,
        transferCategories = true,
    )
}

