package com.example.indexcards.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.indexcards.R
import com.example.indexcards.data.Box
import com.example.indexcards.utils.getCutString
import com.example.indexcards.utils.state.emptyBox
import com.example.indexcards.utils.state.getImageId

@Composable
fun BoxList(
    modifier: Modifier = Modifier,
    boxList: List<Box>,
    isSelecting: Boolean,
    selectedBoxes: List<Box>,
    navigateToBoxScreen: (Long) -> Unit = {},
    startSelection: () -> Unit = {},
    selectBox: (Box) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        if (boxList.isEmpty()) {
            Spacer(modifier = Modifier.size(20.dp))
            Text(
                text = stringResource(R.string.click_to_add_box),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
            ) {
                itemsIndexed(
                    items = boxList
                ) { index, item ->

                    val finalOffset = if (index == boxList.size - 1) {
                        (2.5 * FloatingActionButtonDefaults.LargeIconSize.value).dp
                    } else {
                        0.dp
                    }

                    BoxListItem(
                        modifier = Modifier.padding(bottom = finalOffset),
                        box = item,
                        isSelecting = isSelecting,
                        isSelected = selectedBoxes.contains(item),
                        onClick = {
                            if (isSelecting) {
                                selectBox(item)
                            } else {
                                navigateToBoxScreen(item.boxId)
                            }
                        },
                        onLongClick = {
                            startSelection()
                            selectBox(item)
                        },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BoxListItem(
    modifier: Modifier = Modifier,
    box: Box,
    isSelecting: Boolean,
    isSelected: Boolean,
    onClick: (Box) -> Unit = {},
    onLongClick: (Box) -> Unit = {},
) {
    val context = LocalContext.current
    val imageId = box.getImageId(context)
    val description = box.description.getCutString(40)

    Card(
        modifier = modifier
            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
            .clip(CardDefaults.shape)
            .combinedClickable(
                onClick = { onClick(box) },
                onLongClick = { onLongClick(box) }
            )
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSelecting) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onClick(box) }
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(if (isSelecting) 0.dp else 10.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = box.name,
                    fontWeight = FontWeight(550),
                    style = MaterialTheme.typography.titleLarge,
                )
                if (description.isNotBlank()) {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            if (imageId != -1) {
                Image(
                    painter = painterResource(id = imageId),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(AssistChipDefaults.IconSize * 3F),
                )
            }
        }
    }
}

@Preview
@Composable
fun BoxListItemPreview() {
    BoxListItem(
        isSelecting = false,
        isSelected = false,
        box = emptyBox.copy(name = "Test Box", topic = "Japanese", description = "beschreibung")
    )
}

@Preview
@Composable
fun BoxListItemWithoutDescriptionPreview() {
    BoxListItem(
        isSelecting = false,
        isSelected = false,
        box = emptyBox.copy(name = "Test Box", topic = "Japanese", description = "")
    )
}

@Preview
@Composable
fun BoxListItemSelectedPreview() {
    BoxListItem(
        isSelecting = true,
        isSelected = false,
        box = emptyBox.copy(name = "Test Box", topic = "Japanese", description = "beschreibung")
    )
}

@Preview(showBackground = true)
@Composable
fun BoxListPreview() {
    BoxList(
        modifier = Modifier.height(400.dp),
        isSelecting = false,
        selectedBoxes = listOf(),
        boxList = listOf(
            emptyBox.copy(name = "Box123", description = "", topic = "English"),
            emptyBox.copy(
                name = "Another Box",
                topic = "Chinese",
                description = "a longer description with more words, super delicious sea food"
            ),
        )
    )
}