package com.example.indexcards.ui.box

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.indexcards.R
import com.example.indexcards.data.Card
import com.example.indexcards.data.CardWithTags
import com.example.indexcards.data.Category
import com.example.indexcards.data.Tag
import com.example.indexcards.utils.box.UiBoxWithCategories
import com.example.indexcards.utils.state.emptyCard
import com.example.indexcards.utils.state.emptyTag

@Composable
fun CardList(
    modifier: Modifier = Modifier,
    cardWithTagList: List<CardWithTags>,
    boxWithCategories: UiBoxWithCategories,
    showCategories: Boolean,
    numberOfButtons: Int,
    showCardDialog: (Card) -> Unit = {},
    trainCategory: (Long) -> Unit = {},
) {
    var categoriesExpanded: List<Long> by remember { mutableStateOf(listOf()) }

    if (showCategories) {
        val noCategoryCards = cardWithTagList.filter { it.card.categoryId == (-1).toLong() }
        var noCategoryExpanded by remember { mutableStateOf(false) }

        LazyColumn(
            modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Top
        ) {
            boxWithCategories.categoryList.forEachIndexed { index, category ->
                val categoryFinalOffset = if (
                    index == boxWithCategories.categoryList.size - 1 &&
                    categoriesExpanded.isEmpty() &&
                    noCategoryCards.isEmpty()
                ) {
                    (numberOfButtons * 2 * FloatingActionButtonDefaults.LargeIconSize.value).dp
                } else {
                    0.dp
                }

                item {
                    CategoryListItem(
                        modifier = Modifier.padding(bottom = categoryFinalOffset),
                        category = category,
                        expanded = categoriesExpanded.contains(category.categoryId),
                        changeExpanded = {
                            categoriesExpanded =
                                if (categoriesExpanded.contains(category.categoryId)) {
                                    categoriesExpanded.minus(category.categoryId)
                                } else {
                                    categoriesExpanded.plus(category.categoryId)
                                }
                        },
                        trainCategory = { trainCategory(category.categoryId) },
                    )
                }

                if (categoriesExpanded.contains(category.categoryId)) {
                    val cardsOfCategory =
                        cardWithTagList.filter { it.card.categoryId == category.categoryId }

                    if (cardsOfCategory.isEmpty()) {
                        item {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 6.dp),
                                textAlign = TextAlign.Center,
                                text = stringResource(id = R.string.no_cards_in_category),
                                fontStyle = FontStyle.Italic
                            )
                        }
                    } else {
                        itemsIndexed(cardsOfCategory) { ind, item ->
                            val cardFinalOffset = if (
                                index == boxWithCategories.categoryList.size - 1 &&
                                ind == cardsOfCategory.size - 1 &&
                                noCategoryCards.isEmpty()
                            ) {
                                (numberOfButtons * 2 * FloatingActionButtonDefaults.LargeIconSize.value).dp
                            } else {
                                0.dp
                            }

                            CardListItem(
                                modifier = Modifier.padding(bottom = cardFinalOffset),
                                cardWithTags = item,
                                onClick = { showCardDialog(it) }
                            )
                        }
                    }
                }
            }

            if (noCategoryCards.isNotEmpty()) {
                val categoryFinalOffset = if (!noCategoryExpanded) {
                    (numberOfButtons * 2 * FloatingActionButtonDefaults.LargeIconSize.value).dp
                } else {
                    0.dp
                }

                item {
                    CategoryListItem(
                        modifier = Modifier.padding(bottom = categoryFinalOffset),
                        category = Category(
                            categoryId = -1,
                            boxId = boxWithCategories.box.boxId,
                            name = stringResource(id = R.string.no_category)
                        ),
                        expanded = noCategoryExpanded,
                        changeExpanded = { noCategoryExpanded = !noCategoryExpanded },
                        trainCategory = { trainCategory((-1).toLong()) },
                    )
                }

                if (noCategoryExpanded) {
                    itemsIndexed(noCategoryCards) { index, item ->
                        val cardFinalOffset = if (index == noCategoryCards.size - 1) {
                            (numberOfButtons * 2 * FloatingActionButtonDefaults.LargeIconSize.value).dp
                        } else {
                            0.dp
                        }

                        CardListItem(
                            modifier = Modifier.padding(bottom = cardFinalOffset),
                            cardWithTags = item,
                            onClick = { showCardDialog(it) }
                        )
                    }
                }
            }
        }

        /** If now categories should be shown - old version */
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Top
        ) {
            itemsIndexed(items = cardWithTagList) { index, item ->
                val finalOffset = if (index == cardWithTagList.size - 1) {
                    (numberOfButtons * 2 * FloatingActionButtonDefaults.LargeIconSize.value).dp
                } else {
                    0.dp
                }

                CardListItem(
                    modifier = Modifier.padding(bottom = finalOffset),
                    cardWithTags = item,
                    onClick = { showCardDialog(it) },
                )
            }
        }
    }
}

@Composable
fun CategoryListItem(
    modifier: Modifier = Modifier,
    category: Category,
    expanded: Boolean,
    changeExpanded: () -> Unit = {},
    trainCategory: () -> Unit = {},
) {
    val rotation = if (expanded) {
        0F
    } else {
        -90F
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        HorizontalDivider()

        Row(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .clickable { changeExpanded() }
                .padding(top = 8.dp, bottom = 8.dp, end = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.rotate(rotation),
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "arrow"
            )
            Text(
                modifier = Modifier.padding(end = 14.dp),
                text = category.name,
                fontWeight = FontWeight.Bold,
            )
            HorizontalDivider(
                modifier = Modifier.weight(1F),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8F)
            )
            IconButton(
                modifier = Modifier
                    .border(
                        width = DividerDefaults.Thickness,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8F),
                        shape = CircleShape
                    )
                    .size(34.dp),
                onClick = { trainCategory() }
            ) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "train")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryListItemPreview() {
    CategoryListItem(
        category = Category(categoryId = 1, boxId = 1, name = "Test123"), expanded = false
    )
}

@Preview(showBackground = true)
@Composable
fun CategoryListItemExpandedPreview() {
    CategoryListItem(
        category = Category(categoryId = 1, boxId = 1, name = "Test123"), expanded = true
    )
}

@Composable
fun CardListItem(
    modifier: Modifier = Modifier,
    cardWithTags: CardWithTags,
    onClick: (Card) -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .clickable { onClick(cardWithTags.card) }
            .padding(start = 20.dp, top = 6.dp, bottom = 6.dp, end = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = cardWithTags.card.word,
            textAlign = TextAlign.Start,
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (cardWithTags.card.memoURI.isNotBlank()) {
                Icon(
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .size(22.dp),
                    imageVector = Icons.Default.Mic,
                    contentDescription = "micIcon"
                )

                VerticalDivider(
                    modifier = Modifier
                        .height(30.dp)
                        .padding(start = 3.dp, end = 3.dp)
                )
            }

            if (cardWithTags.tags.isNotEmpty()) {
                if (cardWithTags.tags.size <= 3) {
                    TagCircleRow(tagList = cardWithTags.tags)
                } else {
                    CompactTagCircleRow(tagList = cardWithTags.tags)
                }

                VerticalDivider(
                    modifier = Modifier
                        .height(30.dp)
                        .padding(start = 3.dp, end = 6.dp)
                )
            }

            LevelIndicator(level = cardWithTags.card.level)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardListItemPreview() {
    CardListItem(
        cardWithTags = CardWithTags(
            card = emptyCard.copy(word = "Vorderseite", memoURI = "nonzero", level = 1),
            tags = listOf(
                emptyTag.copy(tagId = 1, text = "Tag1"),
                emptyTag.copy(tagId = 2, text = "Tag2"),
                emptyTag.copy(tagId = 3, text = "Tag3"),
            )
        )
    )
}

@Composable
fun TagCircleRow(
    modifier: Modifier = Modifier, tagList: List<Tag> = listOf()
) {
    Row(
        modifier = modifier, horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        tagList.forEach {
            Canvas(
                modifier = modifier.size(20.dp)
            ) {
                drawCircle(
                    color = Color(android.graphics.Color.parseColor(it.color)), radius = 20f
                )
            }
        }
    }
}

@Composable
fun CompactTagCircleRow(
    modifier: Modifier = Modifier, tagList: List<Tag> = listOf()
) {
    Row(
        modifier = modifier, horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        tagList.forEach {
            Canvas(
                modifier = modifier
                    .clip(RoundedCornerShape(3.dp))
                    .height(20.dp)
                    .width(5.dp)
            ) {
                drawRect(
                    color = Color(android.graphics.Color.parseColor(it.color)),
                )
            }
            Spacer(modifier = modifier.size(2.dp))
        }
    }
}

@Composable
fun LevelIndicator(
    modifier: Modifier = Modifier, level: Int
) {
    Row(
        modifier = modifier, horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (k in 0..4) {
            val color = if (k <= level) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.25F)
            }
            Canvas(
                modifier = modifier
                    .clip(RoundedCornerShape(2.dp))
                    .height(20.dp)
                    .width(3.dp)
            ) {
                drawRect(
                    color = color,
                )
            }
            Spacer(modifier = modifier.size(2.dp))
        }
    }

}