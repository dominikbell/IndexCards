package com.example.indexcards.ui.box.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.indexcards.R
import com.example.indexcards.data.Card
import com.example.indexcards.data.Tag
import com.example.indexcards.ui.box.TagList
import com.example.indexcards.utils.box.UiBoxWithCategories
import com.example.indexcards.utils.box.UiBoxWithTags
import com.example.indexcards.utils.state.BoxDetails
import com.example.indexcards.utils.state.emptyCard
import com.example.indexcards.utils.state.emptyTag
import com.example.indexcards.utils.state.toBox


@Composable
fun TagsToCardsDialog(
    modifier: Modifier,
    boxWithTags: UiBoxWithTags,
    cardList: List<Card>,
    givenTags: List<Tag>,
    onDismiss: () -> Unit = {},
    onSave: (List<Tag>, List<Tag>) -> Unit = { _, _ -> },
    showEditTagDialog: (Tag) -> Unit = {},
) {
    var selectedTags by remember { mutableStateOf<List<Tag>>(listOf()) }
    var deselectedTags by remember { mutableStateOf<List<Tag>>(listOf()) }

    AlertDialog(
        modifier = modifier,
        title = {
            Text(text = stringResource(R.string.editing_tags))
        },
        text = {
            Column {
                Text(stringResource(R.string.editing_tags_text))

                Column(modifier = Modifier.padding(start = 8.dp, top = 2.dp, bottom = 8.dp)) {
                    for (card in cardList) {
                        Text(
                            text = "\u2022 " + card.word,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }

                /** Common tags of all cards */
                if (givenTags.isNotEmpty()) {
                    Column {
                        Text(
                            text = stringResource(R.string.common_tags)
                        )

                        TagList(
                            modifier = Modifier,
                            tagList = givenTags,
                            onClick = {
                                deselectedTags =
                                    if (deselectedTags.contains(it)) {
                                        deselectedTags.minus(it)
                                    } else {
                                        deselectedTags.plus(it)
                                    }
                            },
                            onLongClick = {
                                showEditTagDialog(it)
                            },
                            selectedTags = deselectedTags,
                            onBoxScreen = false,
                        )
                    }
                }

                /** All tags of the box (minus common ones) */
                Column {
                    Text(text = stringResource(R.string.all_tags))

                    TagList(
                        modifier = Modifier,
                        tagList = boxWithTags.tagList.minus(givenTags),
                        onClick = {
                            selectedTags =
                                if (selectedTags.contains(it)) {
                                    selectedTags.minus(it)
                                } else {
                                    selectedTags.plus(it)
                                }
                        },
                        onLongClick = {
                            showEditTagDialog(it)
                        },
                        selectedTags = selectedTags,
                        onBoxScreen = false,
                    )
                }

                /** Showing tags to be added */
                Column(
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Text(stringResource(R.string.adding_tags_text))

                    if (selectedTags.isNotEmpty()) {
                        Row(modifier = Modifier.padding(start = 4.dp)) {
                            selectedTags.map {
                                Text(text = it.text)
                            }
                        }
                    } else {
                        Text(text = "-")
                    }
                }

                /** Showing tags to be removed */
                Column(
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Text(stringResource(R.string.removing_tags_text))

                    if (deselectedTags.isNotEmpty()) {
                        Row(modifier = Modifier.padding(start = 4.dp)) {
                            deselectedTags.map {
                                Text(text = it.text)
                            }
                        }
                    } else {
                        Text(text = "-")
                    }
                }
            }
        },
        onDismissRequest = onDismiss,
        confirmButton =
            {
                TextButton(
                    onClick = {
                        onSave(selectedTags, deselectedTags)
                        onDismiss()
                    }
                ) {
                    Text(text = stringResource(R.string.save))
                }
            },
        dismissButton =
            {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text(text = stringResource(R.string.cancel))
                }
            },
    )
}

@Preview
@Composable
fun TagsToCardsDialogPreview() {
    val box = BoxDetails().copy(
        name = "Box 456",
        topic = "Maschinenbau",
        description = "Schreibebiung mit seeeehr langem Text",
        categories = true,
    ).toBox()

    TagsToCardsDialog(
        modifier = Modifier,
        boxWithTags = UiBoxWithTags(
            box = box,
            tagList = listOf(
                emptyTag.copy(tagId = 1, text = "Tag1"),
                emptyTag.copy(tagId = 2, text = "Tag2"),
            )
        ),
        cardList = listOf(
            emptyCard.copy(word = "Karte123"),
            emptyCard.copy(word = "Karte456"),
            emptyCard.copy(word = "Karte897"),
        ),
        givenTags = listOf(
            emptyTag.copy(tagId = 1, text = "Tag1"),
            emptyTag.copy(tagId = 2, text = "nächster Tag"),
            emptyTag.copy(tagId = 3, text = "übermorgen"),
        ),
    )
}


@Composable
fun CardsToCategoryDialog(
    modifier: Modifier,
    boxWithCategories: UiBoxWithCategories,
    cardList: List<Card>,
    onDismiss: () -> Unit = {},
) {
    AlertDialog(
        modifier = modifier,
        title = { },
        text = { },
        onDismissRequest = onDismiss,
        confirmButton =
            {
                TextButton(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text(text = stringResource(R.string.delete))
                }
            },
        dismissButton =
            {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text(text = stringResource(R.string.cancel))
                }
            },
    )
}