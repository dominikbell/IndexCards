package com.example.indexcards.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.indexcards.NUMBER_OF_LEVELS
import com.example.indexcards.R
import com.example.indexcards.data.CardWithTags
import com.example.indexcards.utils.state.emptyCard

@Composable
fun LevelList(
    modifier: Modifier = Modifier,
    cardWithTagList: List<CardWithTags>,
    currentLevel: Int,
    selectLevel: (Int) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {

        HorizontalDivider(modifier = Modifier.fillMaxWidth())

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
                        onClick = { selectLevel(level) },
                        selected = (currentLevel == level)
                    )
                }
            }
        }

        HorizontalDivider(modifier = Modifier.fillMaxWidth())
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
        selectLevel = { }
    )
}

@Composable
fun LevelListItem(
    modifier: Modifier = Modifier,
    level: Int,
    numberOfItems: Int,
    selected: Boolean = false,
    onClick: () -> Unit
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
        Text(
            text = stringResource(id = R.string.level) + " " + (level + 1).toString(),
            fontSize = 18.sp
        )

        Text(
            text = numberOfItemsText,
            fontSize = 12.sp
        )
    }
}
