package com.example.indexcards.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.indexcards.NUMBER_OF_LEVELS
import com.example.indexcards.R
import com.example.indexcards.data.CardWithTags
import com.example.indexcards.utils.notification.getTimeIntervalAtHour
import com.example.indexcards.utils.state.emptyCard
import java.time.ZonedDateTime


@Composable
fun LevelList(
    modifier: Modifier = Modifier,
    cardWithTagList: List<CardWithTags>,
    currentLevel: Int,
    lastReminders: List<Long>,
    reminderIntervals:  List<Pair<Int, String>>,
    reminderTime: Pair<Int, Int>,
    selectLevel: (Int) -> Unit = {},
) {
    val needsTraining = (0..<NUMBER_OF_LEVELS).map { level ->
        if (lastReminders[level] == (-1).toLong()) {
            false
        } else {
            val trainTimeInterval = getTimeIntervalAtHour(
                reminderIntervals = reminderIntervals,
                reminderTime = reminderTime,
                level = level
            )
            (ZonedDateTime.now().toInstant().toEpochMilli() - lastReminders[level]) >= trainTimeInterval
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                items(NUMBER_OF_LEVELS) { level ->
                    LevelListItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(3.dp),
                        level = level,
                        numberOfItems = cardWithTagList.filter { it.card.level == level }.size,
                        selected = (currentLevel == level),
                        needsTraining = needsTraining[level],
                        onClick = { selectLevel(level) },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun LevelListPreview() {
    LevelList(
        cardWithTagList = listOf(
            CardWithTags(
                card = emptyCard.copy(level = 0),
                tags = listOf()
            ),
            CardWithTags(
                card = emptyCard.copy(level = 0),
                tags = listOf()
            ),
            CardWithTags(
                card = emptyCard.copy(level = 1),
                tags = listOf()
            ),
        ),
        currentLevel = -1,
        lastReminders = (1..5).map { (-1).toLong() },
        reminderIntervals = listOf(),
        reminderTime = Pair(0,0),
    )
}

@Composable
fun LevelListItem(
    modifier: Modifier = Modifier,
    level: Int,
    numberOfItems: Int,
    selected: Boolean,
    needsTraining: Boolean,
    onClick: () -> Unit = {},
) {
    val borderThickness =
        if (selected) {
            3.dp
        } else {
            0.dp
        }

    val borderColor =
        if (selected) {
            MaterialTheme.colorScheme.secondary
        } else {
            MaterialTheme.colorScheme.primaryContainer
        }

    val numberOfItemsText =
        if (numberOfItems == 1) {
            numberOfItems.toString() + " " + stringResource(id = R.string.item_in_level)
        } else {
            numberOfItems.toString() + " " + stringResource(id = R.string.items_in_level)
        }

    val text = if (needsTraining) {
        stringResource(id = R.string.level) + " " + (level + 1).toString() + " "
    } else {
        stringResource(id = R.string.level) + " " + (level + 1).toString()
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(3.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable { onClick() }
            .border(
                width = borderThickness,
                color = borderColor,
                shape = RoundedCornerShape(3.dp)
            )
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            Text(
                text = text,
                fontSize = 18.sp,
            )

            if (needsTraining) {
                Icon(
                    imageVector = Icons.Filled.Timer,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 5.dp, y = (-4).dp)
                        .size(14.dp),
                    tint = Color.Yellow.copy(red = 0.9F, green = 0.8F, blue = 0F),
                    contentDescription = "timer",
                )
            }
        }

        Text(
            text = numberOfItemsText,
            fontSize = 12.sp
        )
    }
}
