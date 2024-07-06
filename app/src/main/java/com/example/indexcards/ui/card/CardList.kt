package com.example.indexcards.ui.card

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.indexcards.data.Card
import com.example.indexcards.data.CardWithTags
import com.example.indexcards.data.Tag
import com.example.indexcards.utils.ViewModelProvider
import com.example.indexcards.utils.card.CardViewModel
import com.example.indexcards.utils.card.EditCardViewModel
import com.example.indexcards.utils.card.toCardDetails
import kotlinx.coroutines.launch

@Composable
fun CardList(
    modifier: Modifier = Modifier,
    cardWithTagList: List<CardWithTags>,
    showCardDialog: (Card) -> Unit,
    showEditCardDialog: (Card) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top
    ) {
        itemsIndexed(
            items = cardWithTagList,
        ) { index, item ->

            val finalOffset = if (index == cardWithTagList.size - 1) {
                (2 * FloatingActionButtonDefaults.LargeIconSize.value).dp
            } else {
                0.dp
            }

            CardListItem(
                modifier = Modifier.padding(bottom = finalOffset),
                item = item.card,
                onClick = {
                    showCardDialog(it)
                },
                onLongClick = {
                    showEditCardDialog(it)
                },
                tagList = item.tags
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardListItem(
    modifier: Modifier = Modifier,
    item: Card,
    onClick: (Card) -> Unit,
    onLongClick: (Card) -> Unit,
    tagList: List<Tag>
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .combinedClickable(
                onClick = { onClick(item) },
                onLongClick = { onLongClick(item) }
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = item.word,
                textAlign = TextAlign.Start,
            )

            Row {
                if (tagList.size <= 3) {
                    TagCircleRow(tagList = tagList)
                } else {
                    CompactTagCircleRow(tagList = tagList)
                }

                VerticalDivider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(3.dp)
                )

                LevelIndicator(level = item.level)
            }
        }
    }
}

@Composable
fun TagCircleRow(
    modifier: Modifier = Modifier,
    tagList: List<Tag> = listOf()
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        tagList.forEach {
            Canvas(
                modifier = modifier
                    .size(20.dp)
            ) {
                drawCircle(
                    color = Color(android.graphics.Color.parseColor(it.color)),
                    radius = 20f
                )
            }
        }
    }
}

@Composable
fun CompactTagCircleRow(
    modifier: Modifier = Modifier,
    tagList: List<Tag> = listOf()
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
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
    modifier: Modifier = Modifier,
    level: Int
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (k in 0..4) {
            val color =
                if (k <= level) {
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