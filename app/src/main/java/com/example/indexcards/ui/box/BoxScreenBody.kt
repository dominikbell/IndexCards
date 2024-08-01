package com.example.indexcards.ui.box

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.indexcards.R
import com.example.indexcards.data.Card
import com.example.indexcards.data.CardWithTags
import com.example.indexcards.data.Tag
import com.example.indexcards.ui.elements.LevelList
import com.example.indexcards.ui.elements.NewTagButton
import com.example.indexcards.utils.state.BoxDetails
import com.example.indexcards.utils.box.UiBoxWithTags
import com.example.indexcards.utils.box.UiCardsWithTags
import com.example.indexcards.utils.box.UiTagWithCards
import com.example.indexcards.utils.state.toBox
import com.example.indexcards.utils.state.emptyCard
import com.example.indexcards.utils.state.emptyTag
import com.example.indexcards.utils.state.isLanguage
import java.time.LocalDateTime
import java.time.ZoneOffset

@Composable
fun BoxScreenBody(
    modifier: Modifier = Modifier,
    levelSelected: Int,
    boxWithTags: UiBoxWithTags,
    cardsWithTags: UiCardsWithTags,
    tagWithCards: UiTagWithCards,
    filteredCardWithTagList: List<CardWithTags>,
    isSearching: Boolean,
    searchText: String,
    showCardDialog: (Card) -> Unit = {},
    showNewTagDialog: () -> Unit = {},
    onTagLongClick: (Tag) -> Unit = {},
    selectLevel: (Int) -> Unit = {},
    setTagSortedBy: (Tag) -> Unit = {},
    resetTagSortedBy: () -> Unit = {},
    updateSearchText: (String) -> Unit = {},
    onCloseSearch: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (isSearching) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        modifier = Modifier.weight(1F),
                        value = searchText,
                        onValueChange = updateSearchText,
                        placeholder = { Text(text = stringResource(id = R.string.search)) }
                    )

                    IconButton(
                        onClick = onCloseSearch
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "clear"
                        )
                    }
                }
            } else {
                if (!boxWithTags.box.isLanguage()) {
                    Text(
                        text = stringResource(id = R.string.topic) + ": ${boxWithTags.box.topic}",
                        style = MaterialTheme.typography.titleMedium,
                    )
                }

                if (boxWithTags.box.description.isNotBlank()) {
                    Text(
                        modifier = Modifier.padding(top = 2.dp, bottom = 4.dp),
                        text = stringResource(R.string.description) + ": ${boxWithTags.box.description}",
                        textAlign = TextAlign.Start,
                    )
                }

                Text(text = stringResource(R.string.nr_card) + ": ${cardsWithTags.cardWithTagList.size}")
            }
        }

        Spacer(modifier = Modifier.size(4.dp))

        LevelList(
            cardWithTagList = cardsWithTags.cardWithTagList,
            currentLevel = levelSelected,
            selectLevel = { selectLevel(it) },
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
        ) {
            TagList(
                modifier = Modifier.weight(1f),
                tagList = boxWithTags.tagList,
                onClick = {
                    if (tagWithCards.tag == it) {
                        resetTagSortedBy()
                    } else {
                        setTagSortedBy(it)
                    }
                },
                onLongClick = { onTagLongClick(it) },
                selectedTags = listOf(tagWithCards.tag)
            )

            VerticalDivider(
                modifier = Modifier
                    .height(ButtonDefaults.MinHeight)
                    .padding(start = 3.dp, end = 3.dp)
            )

            NewTagButton(
                onClick = showNewTagDialog
            )
        }

        if (cardsWithTags.cardWithTagList.isEmpty()) {
            Spacer(modifier = Modifier.size(4.dp))

            Text(
                text = stringResource(R.string.click_to_add_card),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
            )
        } else {
            CardList(
                cardWithTagList = filteredCardWithTagList,
                showCardDialog = { showCardDialog(it) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BoxScreenBodyPreview() {
    val tagList = listOf(
        emptyTag.copy(tagId = 1, text = "Tag123"),
        emptyTag.copy(tagId = 2, text = "Tag3"),
        emptyTag.copy(tagId = 3, text = "Tag243"),
    )
    val cardWithTagsList = listOf(
        CardWithTags(
            emptyCard.copy(word = "Hello", meaning = "Oho", level = 1),
            tags = tagList
        )
    )
    val cardsWithTags = UiCardsWithTags(
        cardWithTagList = cardWithTagsList
    )
    val boxWithTags = UiBoxWithTags(
        box = BoxDetails().copy(
            name = "Box 456",
            topic = "Maschinenbau",
            description = "Schreibebiung mit seeeehr langem Text"
        ).toBox(),
        tagList = tagList
    )
    BoxScreenBody(
        tagWithCards = UiTagWithCards(
            tag = emptyTag.copy(text = "Tag123")
        ),
        levelSelected = -1,
        isSearching = false,
        searchText = "",
        boxWithTags = boxWithTags,
        cardsWithTags = cardsWithTags,
        filteredCardWithTagList = cardWithTagsList,
    )
}

@Preview(showBackground = true)
@Composable
fun BoxScreenBodySearchingPreview() {
    val tagList = listOf(
        emptyTag.copy(tagId = 1, text = "Tag123"),
        emptyTag.copy(tagId = 2, text = "Tag3"),
        emptyTag.copy(tagId = 3, text = "Tag243"),
    )
    val cardWithTagsList = listOf(
        CardWithTags(
            emptyCard.copy(word = "Hello", meaning = "Oho", level = 1),
            tags = tagList
        )
    )
    val cardsWithTags = UiCardsWithTags(
        cardWithTagList = cardWithTagsList
    )
    val boxWithTags = UiBoxWithTags(
        box = BoxDetails().copy(
            name = "Box 456",
            topic = "Maschinenbau",
            description = "Schreibebiung mit seeeehr langem Text"
        ).toBox(),
        tagList = tagList
    )
    BoxScreenBody(
        tagWithCards = UiTagWithCards(
            tag = emptyTag.copy(text = "Tag123")
        ),
        levelSelected = -1,
        isSearching = true,
        searchText = "Search",
        boxWithTags = boxWithTags,
        cardsWithTags = cardsWithTags,
        filteredCardWithTagList = cardWithTagsList,
    )
}