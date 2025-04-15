package com.example.indexcards.ui.box

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
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
import com.example.indexcards.data.Category
import com.example.indexcards.data.Tag
import com.example.indexcards.ui.elements.LevelList
import com.example.indexcards.ui.elements.NewTagButton
import com.example.indexcards.utils.box.UiBoxWithCategories
import com.example.indexcards.utils.state.BoxDetails
import com.example.indexcards.utils.box.UiBoxWithTags
import com.example.indexcards.utils.box.UiCardsWithTags
import com.example.indexcards.utils.box.UiTagWithCards
import com.example.indexcards.utils.state.toBox
import com.example.indexcards.utils.state.emptyCard
import com.example.indexcards.utils.state.emptyTag
import com.example.indexcards.utils.state.isLanguage


@Composable
fun BoxScreenBody(
    modifier: Modifier = Modifier,
    levelSelected: Int,
    boxWithTags: UiBoxWithTags,
    boxWithCategories: UiBoxWithCategories,
    cardsWithTags: UiCardsWithTags,
    tagWithCards: UiTagWithCards,
    filteredCardWithTagList: List<CardWithTags>,
    isSearching: Boolean,
    searchText: String,
    showCategories: Boolean,
    categoriesExpanded: List<Long>,
    numberOfButtons: Int,
    isSelecting: Boolean,
    selectedCards: List<Card>,
    showCardDialog: (Card) -> Unit = {},
    showNewTagDialog: () -> Unit = {},
    onTagLongClick: (Tag) -> Unit = {},
    selectLevel: (Int) -> Unit = {},
    setTagSortedBy: (Tag) -> Unit = {},
    resetTagSortedBy: () -> Unit = {},
    updateSearchText: (String) -> Unit = {},
    onCloseSearch: () -> Unit = {},
    trainCategory: (Long) -> Unit = {},
    toggleCategoryExpanded: (Long) -> Unit = {},
    selectCard: (Card) -> Unit = {},
    startSelection: () -> Unit = {},
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

        LevelList(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            cardWithTagList = cardsWithTags.cardWithTagList,
            currentLevel = levelSelected,
            selectLevel = { selectLevel(it) },
        )

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
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
                selectedTags = listOf(tagWithCards.tag),
                onBoxScreen = true,
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

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
        )

        if (cardsWithTags.cardWithTagList.isEmpty()) {
            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = stringResource(R.string.click_to_add_card),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
            )
        } else {
            CardList(
                cardWithTagList = filteredCardWithTagList,
                showCategories = showCategories,
                categoriesExpanded = categoriesExpanded,
                boxWithCategories = boxWithCategories,
                showCardDialog = { showCardDialog(it) },
                numberOfButtons = numberOfButtons,
                trainCategory = trainCategory,
                toggleCategoryExpanded = toggleCategoryExpanded,
                isSelecting = isSelecting,
                selectedCards = selectedCards,
                selectCard = selectCard,
                startSelection = startSelection,
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
        boxWithCategories = UiBoxWithCategories(),
        showCategories = false,
        categoriesExpanded = listOf(),
        cardsWithTags = cardsWithTags,
        filteredCardWithTagList = cardWithTagsList,
        numberOfButtons = 1,
        isSelecting = false,
        selectedCards = listOf(),
    )
}

@Preview(showBackground = true)
@Composable
fun BoxScreenBodyCategoriesPreview() {
    val box = BoxDetails().copy(
        name = "Box 456",
        topic = "Maschinenbau",
        description = "Schreibebiung mit seeeehr langem Text"
    ).toBox()

    val category1 = Category(categoryId = 0, boxId = -1, name = "Catta")
    val category2 = Category(categoryId = 1, boxId = -1, name = "Fanstato")

    val tag1 = emptyTag.copy(tagId = 1, text = "Tag123")
    val tag2 = emptyTag.copy(tagId = 2, text = "Tag3")
    val tag3 = emptyTag.copy(tagId = 3, text = "Tag243")

    val tagList = listOf(tag1, tag2, tag3)

    val cardWithTagsList = listOf(
        CardWithTags(
            emptyCard.copy(word = "Hello", meaning = "Oho", memoURI = "asd", level = 1, categoryId = -1),
            tags = tagList
        ),
        CardWithTags(
            emptyCard.copy(word = "SDee", meaning = "asd", level = 1, categoryId = 0),
            tags = tagList.minus(tag1)
        ),
        CardWithTags(
            emptyCard.copy(word = "Plosad", meaning = "Okosd", level = 1, categoryId = 1),
            tags = tagList.minus(tag1).minus(tag3)
        ),
        CardWithTags(
            emptyCard.copy(word = "Messs", meaning = "ploo", level = 1, categoryId = 1),
            tags = tagList.minus(tag2).minus(tag1)
        ),
    )
    val cardsWithTags = UiCardsWithTags(
        cardWithTagList = cardWithTagsList
    )

    val boxWithTags = UiBoxWithTags(
        box = box,
        tagList = tagList
    )
    val boxWithCategories = UiBoxWithCategories(
        box = box,
        categoryList = listOf(category1, category2)
    )

    BoxScreenBody(
        tagWithCards = UiTagWithCards(
            tag = emptyTag.copy(text = "Tag123")
        ),
        levelSelected = -1,
        isSearching = false,
        searchText = "",
        boxWithTags = boxWithTags,
        boxWithCategories = boxWithCategories,
        showCategories = true,
        categoriesExpanded = listOf(0, -1),
        cardsWithTags = cardsWithTags,
        filteredCardWithTagList = cardWithTagsList,
        numberOfButtons = 1,
        isSelecting = false,
        selectedCards = listOf(),
    )
}

@Preview(showBackground = true)
@Composable
fun BoxScreenBodySelectingPreview() {
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
        boxWithCategories = UiBoxWithCategories(),
        showCategories = false,
        categoriesExpanded = listOf(),
        cardsWithTags = cardsWithTags,
        filteredCardWithTagList = cardWithTagsList,
        numberOfButtons = 1,
        isSelecting = true,
        selectedCards = listOf(),
    )
}

@Preview(showBackground = true)
@Composable
fun BoxScreenBodyCategoriesSelectingPreview() {
    val box = BoxDetails().copy(
        name = "Box 456",
        topic = "Maschinenbau",
        description = "Schreibebiung mit seeeehr langem Text"
    ).toBox()

    val category1 = Category(categoryId = 0, boxId = -1, name = "Catta")
    val category2 = Category(categoryId = 1, boxId = -1, name = "Fanstato")

    val tag1 = emptyTag.copy(tagId = 1, text = "Tag123")
    val tag2 = emptyTag.copy(tagId = 2, text = "Tag3")
    val tag3 = emptyTag.copy(tagId = 3, text = "Tag243")

    val tagList = listOf(tag1, tag2, tag3)

    val cardWithTagsList = listOf(
        CardWithTags(
            emptyCard.copy(word = "Hello", meaning = "Oho", memoURI = "asd", level = 1, categoryId = -1),
            tags = tagList
        ),
        CardWithTags(
            emptyCard.copy(word = "SDee", meaning = "asd", level = 1, categoryId = 0),
            tags = tagList.minus(tag1)
        ),
        CardWithTags(
            emptyCard.copy(word = "Plosad", meaning = "Okosd", level = 1, categoryId = 1),
            tags = tagList.minus(tag1).minus(tag3)
        ),
        CardWithTags(
            emptyCard.copy(word = "Messs", meaning = "ploo", level = 1, categoryId = 1),
            tags = tagList.minus(tag2).minus(tag1)
        ),
    )
    val cardsWithTags = UiCardsWithTags(
        cardWithTagList = cardWithTagsList
    )

    val boxWithTags = UiBoxWithTags(
        box = box,
        tagList = tagList
    )
    val boxWithCategories = UiBoxWithCategories(
        box = box,
        categoryList = listOf(category1, category2)
    )

    BoxScreenBody(
        tagWithCards = UiTagWithCards(
            tag = emptyTag.copy(text = "Tag123")
        ),
        levelSelected = -1,
        isSearching = false,
        searchText = "",
        boxWithTags = boxWithTags,
        boxWithCategories = boxWithCategories,
        showCategories = true,
        categoriesExpanded = listOf(0, -1),
        cardsWithTags = cardsWithTags,
        filteredCardWithTagList = cardWithTagsList,
        numberOfButtons = 1,
        isSelecting = true,
        selectedCards = listOf(),
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
        boxWithCategories = UiBoxWithCategories(),
        showCategories = false,
        categoriesExpanded = listOf(),
        cardsWithTags = cardsWithTags,
        filteredCardWithTagList = cardWithTagsList,
        numberOfButtons = 1,
        isSelecting = false,
        selectedCards = listOf(),
    )
}