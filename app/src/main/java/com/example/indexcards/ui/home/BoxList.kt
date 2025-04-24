package com.example.indexcards.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.indexcards.R
import com.example.indexcards.data.Box
import com.example.indexcards.utils.getCutString
import com.example.indexcards.utils.notification.getTimeInterval
import com.example.indexcards.utils.state.emptyBox
import com.example.indexcards.utils.state.getImageId
import java.time.ZonedDateTime


@Composable
fun BoxList(
    modifier: Modifier = Modifier,
    boxList: List<Box>,
    isSelecting: Boolean,
    selectedBoxes: List<Box>,
    reminderIntervals:  List<Pair<Int, String>>,
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
            Text(
                modifier = modifier.padding(top = 20.dp),
                text = stringResource(R.string.click_to_add_box),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                itemsIndexed(
                    items = boxList
                ) { index, box ->

                    val finalOffset = if (index == boxList.size - 1) {
                        (2.5 * FloatingActionButtonDefaults.LargeIconSize.value).dp
                    } else {
                        0.dp
                    }

                    val needsTraining = listOf(
                        box.lastTrained1,
                        box.lastTrained2,
                        box.lastTrained3,
                        box.lastTrained4,
                        box.lastTrained5,
                    ).mapIndexed { ind, lastTrainTime ->
                        if (lastTrainTime == (-1).toLong()) {
                            false
                        } else {
                            val trainTimeInterval = getTimeInterval(
                                reminderIntervals = reminderIntervals,
                                level = ind
                            )
                            (ZonedDateTime.now().toInstant().toEpochMilli() - lastTrainTime) >= trainTimeInterval
                        }
                    }.any { it == true }

                    BoxListItem(
                        modifier = Modifier.padding(bottom = finalOffset),
                        box = box,
                        isSelecting = isSelecting,
                        isSelected = selectedBoxes.contains(box),
                        onClick = {
                            if (isSelecting) {
                                selectBox(box)
                            } else {
                                navigateToBoxScreen(box.boxId)
                            }
                        },
                        onLongClick = {
                            startSelection()
                            selectBox(box)
                        },
                        needsTraining = (needsTraining && !isSelecting),
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
    needsTraining: Boolean,
    onClick: (Box) -> Unit = {},
    onLongClick: (Box) -> Unit = {},
) {
    val context = LocalContext.current
    val imageId = box.getImageId(context)
    val description = box.description.getCutString(40)

    Card(
        modifier = modifier
            .clip(CardDefaults.shape)
            .combinedClickable(
                onClick = { onClick(box) },
                onLongClick = { onLongClick(box) },
            )
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (isSelecting) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onClick(box) },
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = if (isSelecting) 0.dp else 10.dp)
                    .padding(top = 10.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.Top,
            ) {
                Box {
                    Text(
                        text = box.name,
                        fontWeight = FontWeight(550),
                        style = MaterialTheme.typography.titleLarge,
                    )

                    if (needsTraining) {
                        Icon(
                            imageVector = Icons.Filled.Timer,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 13.dp, y = (-4).dp)
                                .size(14.dp),
                            tint = Color.Yellow.copy(red = 0.9F, green = 0.8F, blue = 0F),
                            contentDescription = "timer",
                        )
                    }
                }
                if (description.isNotBlank()) {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
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
        box = emptyBox.copy(name = "Test Box", topic = "Japanese", description = "beschreibung"),
        needsTraining = false,
    )
}

@Preview
@Composable
fun BoxListItemWithoutDescriptionPreview() {
    BoxListItem(
        isSelecting = false,
        isSelected = false,
        box = emptyBox.copy(name = "Test Box", topic = "Japanese", description = ""),
        needsTraining = false,
    )
}

@Preview
@Composable
fun BoxListItemSelectedPreview() {
    BoxListItem(
        isSelecting = true,
        isSelected = false,
        box = emptyBox.copy(name = "Test Box", topic = "Japanese", description = "beschreibung"),
        needsTraining = false,
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
        ),
        reminderIntervals = listOf(),
    )
}